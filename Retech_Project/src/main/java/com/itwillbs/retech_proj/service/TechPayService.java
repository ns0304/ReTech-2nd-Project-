package com.itwillbs.retech_proj.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.retech_proj.handler.BankApiClient;
import com.itwillbs.retech_proj.mapper.TechPayMapper;
import com.itwillbs.retech_proj.vo.BankToken;

@Service
public class TechPayService {

	@Autowired 
	private BankApiClient bankApiClient;
	
	@Autowired
	private TechPayMapper mapper;
	
	// 핀테크 사용자 정보 조회(DB)
	public BankToken getBankUserInfo(String id) {
		// TechPayMapper - selectBankUserInfo()
		return mapper.selectBankUserInfo(id);
	}

	// 엑세스토큰 발급 요청
	public BankToken getAccessToken(Map<String, String> authResponse) {
		// BankApiClient - requestAccessToken() 메서드 호출하여 엑세스토큰 발급 요청 수행
		return bankApiClient.requestAccessToken(authResponse);
	}

	public void registAccessToken(Map<String, Object> map) {
		// TechPayMapper - selectTokenInfo() 메서드 호출하여 아이디에 해당하는 토큰 정보 조회
		String id = mapper.selectId(map);
		System.out.println("토큰 아이디 정보 : " + id);
		
		// 조회된 아이디가 없을 경우(= 엑세스토큰 정보 없음) 새 엑세스토큰 정보 추가(INSERT) - insertAccessToken()
		// 조회된 아이디가 있을 경우(= 엑세스토큰 정보 있음) 새 엑세스토큰 정보 갱신(UPDATE) - updateAccessToken()
		if(id == null) {
			mapper.insertAccessToken(map);
		} else {
			mapper.updateAccessToken(map);
		}
	}
	
	
	
}