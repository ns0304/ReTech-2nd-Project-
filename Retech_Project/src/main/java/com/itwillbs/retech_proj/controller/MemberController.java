package com.itwillbs.retech_proj.controller;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.retech_proj.handler.RsaKeyGenerator;
import com.itwillbs.retech_proj.service.MemberService;
import com.itwillbs.retech_proj.service.ProductService;
import com.itwillbs.retech_proj.vo.MemberVO;
import com.itwillbs.retech_proj.vo.ProductVO;


@Controller
public class MemberController {
	@Autowired
	   private MemberService service;

	   // 회원 가입 -------------------------------------------------------------------------------------------
	   @GetMapping("MemberJoin")
	   public String memberJoin() {
		   
	      return "member/member_join";
	   }

	   @PostMapping("MemberJoin")
	   public String memberDupId(MemberVO member, Model model, String rememberId, BCryptPasswordEncoder passwordEncoder) {
		  String securePasswd = passwordEncoder.encode(member.getMember_passwd());
		  System.out.println("평문 : " + member.getMember_passwd()); // admin123
		  System.out.println("암호문 : " + securePasswd); // $2a$10$hw02bLaTVPfeCbZ3vdXU0uWDZu52Ov1rof5pZCFkngtuA5Ld9BSxq
		  // => 단, 매번 생성되는 암호문은 솔트(Salt)값에 의해 항상 달라진다!
			
		  // 3. 암호화 된 패스워드를 다시 MemberVO 객체의 passwd 값에 저장(덮어쓰기)
		  member.setMember_passwd(securePasswd);
			
			
		  MemberVO dbmember = service.getMember(member);
			  
		  System.out.println("찾은 id : " + dbmember);
		  String member_id = member.getMember_id();
		  
		  if(dbmember != null) {
			  model.addAttribute("msg", "중복되는 아이디입니다");
			  
			  return "result/fail";
		  } else {
			  return "redirect:/MemberJoinForm?member_id=" + member_id;
		  }
	   }
	   
	   @GetMapping("MemberJoinForm")
	   public String memberJoinForm(@RequestParam(defaultValue = "") String member_id) {
//		  System.out.println("넘어온 member_id 확인 : " + member_id);
	      return "member/member_join_form";
	   }

	   @PostMapping("MemberJoinForm")
	   public String memberJoinForm(MemberVO member, Model model, BCryptPasswordEncoder passwordEncoder) {
	      System.out.println(member);
	      
	      // 전화번호 중복 체크
	      if (service.isExistPhonenumber(member) != null) {
	          model.addAttribute("msg", "이미 등록된 전화번호입니다.");
	          return "result/fail"; // 실패 페이지로 이동
	      }
	      
	      String securePasswd = passwordEncoder.encode(member.getMember_passwd());
	      member.setMember_passwd(securePasswd);
	      int insertCount = service.registMember(member);
	      if (insertCount > 0) {
	         return "redirect:/MemberJoinSuccess";
	      } else {
	         model.addAttribute("msg", "회원가입에 실패하였습니다. 정보를 확인해주세요.");
	         return "result/fail";
	      }
	   }

	   @GetMapping("MemberJoinSuccess")
	   public String memberJoinSuccess() {
	      return "member/member_join_success";
	   }

	   // 로그인 -------------------------------------------------------------------------------------------
	   @GetMapping("MemberLogin")
	   public String memberLogin(HttpSession session, Model model) {
//		   아이디와 패스워드 암호화 과정에서 사용할 공개키/개인키 생성
		   Map<String, Object> rsaKey = RsaKeyGenerator.generateKey();
		   session.setAttribute("RSAPrivateKey", rsaKey.get("RSAPrivateKey"));
		   model.addAttribute("RSAModulus", rsaKey.get("RSAModulus"));
		   model.addAttribute("RSAExponent", rsaKey.get("RSAExponent"));
		   
		   return "member/member_login_form";
	   }

	   @PostMapping("MemberLogin")
	   public String loginPro(MemberVO member, BCryptPasswordEncoder passwordEncoder, Model model,
		        HttpSession session, HttpServletResponse response, String rememberId) throws Exception {
		   	
//		   	System.out.println(member);
//			System.out.println("아이디 기억 : " + rememberId); // 체크 : "on" , 미체크 : null
//			 =============================== 아이디/패스워드 복호화 ===============================
			System.out.println("암호화 된 아이디 : " + member.getMember_id());
			System.out.println("암호화 된 패스워드 : " + member.getMember_passwd());
			
			// 세션에서 개인키 가져오기
			PrivateKey privateKey = (PrivateKey)session.getAttribute("RSAPrivateKey");
			
			// RsaKeyGenerator 클래스의 decrypt() 메서드 호출하여 전달받은 암호문 복호화
			// => 파라미터 : 세션에 저장된 개인키, 암호문   리턴타입 : String
			String id = RsaKeyGenerator.decrypt(privateKey, member.getMember_id());
			String passwd = RsaKeyGenerator.decrypt(privateKey, member.getMember_passwd());
			System.out.println("복호화 된 아이디 : " + id);
			System.out.println("복호화 된 패스워드 : " + passwd);
			
			// MemberVO 객체에 복호화 된 아이디, 패스워드 저장
			member.setMember_id(id);
			member.setMember_passwd(passwd);	   
		   
		    MemberVO dbMember = service.getMember(member);
		    
		    if (dbMember == null || !passwordEncoder.matches(member.getMember_passwd(), dbMember.getMember_passwd())) {
		        model.addAttribute("msg", "로그인 실패!");
		        return "result/fail";
		    } else if (dbMember.getMember_status().equals("탈퇴")) {
		        model.addAttribute("msg", "탈퇴한 회원입니다!");
		        return "result/fail";
		    } else {
		        // 로그인 성공 시, 세션에 상점명과 관련된 정보 저장
		        session.setAttribute("sId", member.getMember_id());
		        session.setAttribute("sName", dbMember.getMember_name()); // 세션에 회원 이름 저장
		        session.setAttribute("sNickName", dbMember.getMember_nickname()); // 세션에 상점명 저장
		        session.setAttribute("sIsAdmin", dbMember.getMember_isAdmin());
		        session.setMaxInactiveInterval(3600);

		        Cookie cookie = new Cookie("rememberId", member.getMember_id());
		        if (rememberId != null) {
		            cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
		        } else {
		            cookie.setMaxAge(0);
		        }
		        response.addCookie(cookie);

		        if (session.getAttribute("prevURL") == null) {
		            return "redirect:/";
		        } else {
		            return "redirect:" + session.getAttribute("prevURL");
		        }
		    }
			
		}
	   
	   
	   
	   // 로그아웃 -------------------------------------------------------------------------------------------
	   @GetMapping("MemberLogout")
	   public String logout(HttpSession session) {
	      session.invalidate();
	      return "redirect:/";
	   }

	   // 아이디, 비밀번호 찾기 -------------------------------------------------------------------------------
	   // 아이디 찾기
		@GetMapping("MemberSearchId")
		public String searchId() {
			
			return "member/member_search_id";
		}
		
		@PostMapping("SearchIdPro")
		public String searchIdPro(MemberVO member, Model model) {
//			System.out.println(member);
			MemberVO dbMember = service.getMemberSearchId(member); // DB에 저장된 회원정보 가져오기
//		    System.out.println("dbMember : " + dbMember);
			
			if(dbMember == null || dbMember.getMember_status().equals("탈퇴")) { // 회원정보 없을 때 or 탈퇴한 회원일 때
				model.addAttribute("msg", "조회결과가 없습니다.");
				
		        return "result/fail";
			}
			
			if (!member.getMember_name().equals(dbMember.getMember_name()) || 
					!member.getMember_birth().equals(dbMember.getMember_birth()) ||
					!member.getMember_phone().equals(dbMember.getMember_phone())) { // 정보가 하나라도 맞지 않으면 찾을 수 없어야함
				
				model.addAttribute("msg", "회원을 찾을 수 없습니다. 입력하신 정보를 확인해주세요.");
				
		        return "result/fail";
		        
			} else if(member.getMember_name().equals(dbMember.getMember_name()) && 
				member.getMember_birth().equals(dbMember.getMember_birth()) &&
				member.getMember_phone().equals(dbMember.getMember_phone())) { // 회원이 입력한 정보와 DB에 저장된 정보가 같을 때 ! 성공 !
				
				String member_id = dbMember.getMember_id();
				
				return "redirect:/SearchIdSuccess?member_id=" + member_id;
			}
			
			return "";  
		}
		
		@GetMapping("SearchIdSuccess")
		public String searchIdSuccess(String member_id, Model model) {
			System.out.println(member_id);
			
			model.addAttribute("member_id", member_id);
			
			return "member/member_search_id_success";
		}
		
		// 비밀번호 찾기 페이지
				@GetMapping("Passwd_find") 
				public String passwd_find() {
					return "member/member_pw_find";
				}
				
				// 비밀번호 찾기2 페이지
				@PostMapping("PwFindPro")
				public String pw_find_pro(MemberVO member, Model model) {
					
					MemberVO dbMember = service.isExistId(member);
					
					if(dbMember == null) { 
						model.addAttribute("msg", "없는 아이디입니다");
						return "result/fail";

					} else {
//						model.addAttribute("mem_id", mem_id); // model에 아이디값 저장
						model.addAttribute("dbMember", dbMember); // model에 아이디값 저장
						return "member/member_pw_find_pro";
					}
					
				}
			
				// 전화번호로 비밀번호 찾기
				@PostMapping("PwResetPro")
				public String pwResetPro(MemberVO member, Model model) {
					MemberVO dbMember = service.isExistPhonenumber(member);
			
					if(dbMember == null) { // !member.getMem_tel().equals(mem_tel)
						model.addAttribute("msg", "없는 전화번호입니다");
						return "result/fail";
						
					} else {
						model.addAttribute("dbMember", dbMember); // model에 전화번호값 저장
						return "member/member_pw_reset";
					}
					
				}
				// 비밀번호 재설정
				@PostMapping("PwResetFinal")
				public String pwResetFinal(@RequestParam Map<String, String> map, MemberVO member,
				                           BCryptPasswordEncoder passwordEncoder, Model model) {
				    // member 정보가 null이 아닌지 확인하여 NullPointerException 방지  
				    if (member != null) {
				        member = service.getMember(member); // 기존 member 정보 조회
				    } else {
				        model.addAttribute("msg", "회원 정보를 찾을 수 없습니다.");
				        return "result/fail";
				    }

				    // 새 비밀번호 입력 여부를 확인하여 새 비밀번호 입력됐을 경우 암호화 수행 필요
				    String newPasswd = map.get("member_passwd");
				    if (newPasswd != null && !newPasswd.isEmpty()) {
				        map.put("member_passwd", passwordEncoder.encode(newPasswd)); // 새 비밀번호 암호화
				        System.out.println("map : " + map); // passwd 항목 암호화 결과 확인
				    }

				    // 회원 정보 수정
				    int updateCount = service.modifyMember(map);

				    if (updateCount > 0) {
				        model.addAttribute("msg", "패스워드 수정 성공!");
				        model.addAttribute("targetURL", "MemberLogin");
				        return "result/success";
				    } else {
				        model.addAttribute("msg", "패스워드 수정 실패!");
				        return "result/fail";
				    }
				}
	
				
	   @GetMapping("MyPageMain")
	   public String mypageinfo2(@RequestParam Map<String, String> map, HttpSession session, MemberVO member, BCryptPasswordEncoder passwordEncoder, Model model) {
		   String id = (String) session.getAttribute("sId");
		   // 세션에 사용자 ID가 존재하는 경우
		   if (id != null) {
			   member.setMember_id(id);
			   // 해당 ID의 회원 정보를 조회
			   member = service.getMember(member);
			   model.addAttribute("member", member);
		   }
		   return "mypage/member_mypage";
	   }
				
//	   @PostMapping("MyPageMain")
//	   public String mypageinfo2(@RequestParam Map<String, String> map, MemberVO member, BCryptPasswordEncoder passwordEncoder, Model model) {
//		     
//		   return "member/member_mypage";
//	   }


	   
	   @GetMapping("MemberInfo")
	   public String memberInfo(MemberVO member, HttpSession session, Model model) {
	      String id = (String)session.getAttribute("sId");
	      if (id == null) {
	         model.addAttribute("msg", "로그인 필수!");
	         model.addAttribute("targetURL", "MemberLogin");
	         return "result/fail";
	      } else {
	         member.setMember_id(id);
	         member = service.getMember(member);
	         model.addAttribute("member", member);
	         return "member/member_info";
	      }
	   }
	   
	   @GetMapping("MemberInfo2")
	   public String memberInfo2(MemberVO member, Model model) {
		   
		   member = service.getMember(member);
		   
		   model.addAttribute("member", member);
		   
		   return "member/member_info";
	   }

//	   @PostMapping("MemberModify")
//	   public String mypageinfo(@RequestParam Map<String, String> map, MemberVO member, BCryptPasswordEncoder passwordEncoder, Model model) {
//		   System.out.println(member);
//		   System.out.println(map);
//	      member =service.getMember(member);
//	      if (!passwordEncoder.matches((CharSequence)map.get("member_oldpw"), member.getMember_passwd())) {
//	         model.addAttribute("msg", "수정 권한이 없습니다!");
//	         return "result/fail";
//	      } else {
//	         if (!((String)map.get("member_passwd")).equals("")) {
//	            map.put("member_passwd", passwordEncoder.encode((CharSequence)map.get("member_passwd")));
//	         }
//
//	         int updateCount = service.modifyMember(map);
//	         if (updateCount > 0) {
//	            model.addAttribute("msg", "회원정보 수정 성공!");
//	            model.addAttribute("targetURL", "MemberInfo");
//	            return "result/success";
//	         } else {
//	            model.addAttribute("msg", "회원정보 수정 실패!");
//	            return "result/fail";
//	         }
//	      }
//	   }
	   
	   // 회원정보 수정
	   @PostMapping("MemberModify")
	   public String mypageinfo(@RequestParam Map<String, String> map, MemberVO member, BCryptPasswordEncoder passwordEncoder, Model model) {
	       if (member == null || member.getMember_id() == null) {
	           model.addAttribute("msg", "회원 정보를 찾을 수 없습니다.");
	           return "result/fail";
	       }

	       member = service.getMember(member);
	       if (member == null) {
	           model.addAttribute("msg", "회원 정보 조회 실패!");
	           return "result/fail";
	       }

	       String oldPassword = map.get("member_oldpw");
	       if (oldPassword == null || !passwordEncoder.matches(oldPassword, member.getMember_passwd())) {
	           model.addAttribute("msg", "수정 권한이 없습니다!");
	           return "result/fail";
	       }

	       String newPassword = map.get("member_passwd");
	       if (newPassword != null && !newPassword.isEmpty()) {
	           map.put("member_passwd", passwordEncoder.encode(newPassword));
	       } else {
	           map.remove("member_passwd");  // 비밀번호가 없으면 맵에서 제거
	       }

	       int updateCount = service.modifyMember(map);
	       if (updateCount > 0) {
	           model.addAttribute("msg", "회원정보 수정 성공!");
	           model.addAttribute("targetURL", "MemberInfo");
	           return "result/success";
	       } else {
	           model.addAttribute("msg", "회원정보 수정 실패!");
	           return "result/fail";
	       }
	   }
	 	   
	   	   
	   // 회원 탈퇴 
	   @GetMapping("MemberWithdraw")
	   public String withdrawForm(HttpSession session, Model model) {
	      String id = (String)session.getAttribute("sId");
	      if (id == null) {
	         model.addAttribute("msg", "로그인 필수!");
	         model.addAttribute("targetURL", "MemberLogin");
	         return "result/fail";
	      } else {
	         return "member/member_withdraw_form";
	      }
	   }

	   @PostMapping("MemberWithdraw")
	   public String withdrawPro(MemberVO member, HttpSession session, Model model, BCryptPasswordEncoder passwordEncoder) {
	      String id = (String)session.getAttribute("sId");
	      if (id == null) {
	         model.addAttribute("msg", "로그인 필수!");
	         model.addAttribute("targetURL", "MemberLogin");
	         return "result/fail";
	      } else {
	         member.setMember_id(id);
	         MemberVO dbMember = service.getMember(member);
	         if (!passwordEncoder.matches(member.getMember_passwd(), dbMember.getMember_passwd())) {
	            model.addAttribute("msg", "수정 권한이 없습니다!");
	            return "result/fail";
	         } else {
	            this.service.withdrawMember(member);
	            session.invalidate();
	            model.addAttribute("msg", "회원 탈퇴 완료!");
	            model.addAttribute("targetURL", "./");
	            return "result/success";
	         }
	      }
	   }
	   
	   @Autowired
	   private ProductService productService;
	   //판매내역
	   @GetMapping("SaleHistory")
	   public String SaleHistory(@RequestParam(value = "startRow", defaultValue = "0") int startRow,
	                              @RequestParam(value = "listLimit", defaultValue = "10") int listLimit,
	                              Model model, HttpSession session,MemberVO member) {
	    String id = (String) session.getAttribute("sId");
			   // 세션에 사용자 ID가 존재하는 경우
			  if (id != null) {
				  member.setMember_id(id);
				   // 해당 ID의 회원 정보를 조회
				  member = service.getMember(member);
				  model.addAttribute("member", member);
			  }
	    
			  List<ProductVO> productList = productService.getProductList(startRow, listLimit);
			  int totalProductCount = productService.getProductListCount();

			  model.addAttribute("productList", productList);
			  model.addAttribute("totalProductCount", totalProductCount);

	       return "mypage/salehistory";
	    }	   
	
	   // 구매내역
	   @GetMapping("PurchaseHistory")
	   public String Purchasehistory(@RequestParam(value = "startRow", defaultValue = "0") int startRow,
						             @RequestParam(value = "listLimit", defaultValue = "10") int listLimit,
									 Model model, HttpSession session,MemberVO member) {
		   
		   String id = (String) session.getAttribute("sId");
		   // 세션에 사용자 ID가 존재하는 경우
		   if (id != null) {
			   member.setMember_id(id);
			   // 해당 ID의 회원 정보를 조회
			   member = service.getMember(member);
			   model.addAttribute("member", member);
		   }
		   
		   List<ProductVO> productList = productService.getProductList(startRow, listLimit);
	       int totalProductCount = productService.getProductListCount();

	       model.addAttribute("productList", productList);
	       model.addAttribute("totalProductCount", totalProductCount);

		   
		   return "mypage/purchasehistory";
	   }
	 
	   // 찜한상품
	   @GetMapping("Wishlist")
	   public String Wishlist(Model model, HttpSession session,MemberVO member) {
		   
		   String id = (String) session.getAttribute("sId");
		   // 세션에 사용자 ID가 존재하는 경우
		   if (id != null) {
			   member.setMember_id(id);
			   // 해당 ID의 회원 정보를 조회
			   member = service.getMember(member);
			   model.addAttribute("member", member);
		   }
		   
		   return "mypage/wishlist";
	   }
	   
	   // 거래상태 업데이트
	   @PostMapping("/updateTransactionStatus")
	   @ResponseBody
	   public Map<String, Object> updateTransactionStatus(@RequestParam("id") int productId,
	                                                       @RequestParam("status") String status) {
		   Map<String, Object> response = new HashMap<>();
	       int success = productService.updateProductStatus(productId, status);
	       response.put("success", success);
	       response.put("status", status);
	       return response;
	    }
	   
	   
//	   @GetMapping("Review")
//	   public String review() {
//		return "review_popup";
//		   
//	   }
	   
	   
	   
}
