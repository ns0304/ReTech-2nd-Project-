<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Retech_테크페이</title>
<!-- 자바스크립트 연결 -->
<script src="${pageContext.request.servletContext.contextPath}/resources/js/jquery-3.7.1.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> <!-- Font Awesome 아이콘 라이브러리 로드 -->
<!-- 외부 CSS 파일(css/default.css) 연결 -->
<link href="${pageContext.request.contextPath}/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
/*---- techpay_payments 영역 전체 ----*/
.payments_container {
	max-width: 500px;
	margin: auto;
	margin-bottom: 20px;
	margin-top: 20px;
	border: 1px solid gray;
	border-radius: 10px;
	box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.1);
	padding: 20px 50px;
	box-sizing: border-box;
	display: block;
}
.title, .pay_balance, .payments_amount {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}
/*---- 충전하기버튼 ----*/
.btn_top {
	display: flex;
	justify-content: space-between;
	gap: 10px;
	margin-bottom: 20px;
	margin-top: 20px;
	
}
.payments_btn {
	padding: 10px 20px;
	border: none;
	border-radius: 2px;
	background-color: skyblue;
	color: white;
	cursor: pointer;
	font-size: 14px;
	width: 100%;
	margin-top: 20px;
}
</style>
</head>
<body>
	<header>
		<%-- 기본 메뉴 표시 영역(inc/top.jsp) 페이지 삽입 --%>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>	
	</header>
	<section>
		<div class="payments_container">
	       <!-- 페이 기본 정보 표시  -->
	       <div class="pay_card">
			  <div class="title">
			  	<h2>테크페이 결제</h2>
				<img src="${pageContext.request.servletContext.contextPath}/resources/images/logo.png" height="40" width="80">
			  </div>
			</div>				
          <div class="pay_balance">
	        <h3>${sessionScope.sName}님의 페이잔액</h3>
	        <h3><fmt:formatNumber value="${sessionScope.pay_balance}" pattern="#,###" />원</h3>
          </div>
          <div class="payments_amount">
	        <h2>결제금액</h2>
	        <h2>${paymentAmount}원</h2>
          </div>
          <div class="pay_account_list">	
 		        <table border="1">
		        	<c:forEach var="account" items="${accountList.res_list}">
		        		<tr>
		        			<td>1</td>
		        			<td><b>${account.account_alias}</b><br>
		        				${account.bank_name}<br>
		        				
		        				${account.account_num_masked}<br>
		        			</td>
		        			<td>${account.account_holder_name}</td>
		        			<td>
		        				<form action="TechPaymentsProcess" method="post">
		        					<input type="hidden" name="withdraw_fintech_use_num" value="${account.fintech_use_num}">
		        					<input type="hidden" name="withdraw_client_name" value="${account.account_holder_name}">
		        					<input type="hidden" name="tran_amt" value="110000">
		        					<input type="submit" value="결제하기">
		        				</form>
		        			</td>
		        		</tr>
		        	</c:forEach>	
	        	</table>        		       
	            <button class="payments_btn">결제하기</button>
          </div>
        </div>
	</section>
	<footer>
		<%-- 회사 소개 영역(inc/bottom.jsp) 페이지 삽입 --%>	
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>	
	</footer>
</body>
</html>