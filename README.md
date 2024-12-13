# 🛒retech : 테크기기 중고 거래 사이트 '리테크'




배포주소
http://c5d2403t1.itwillbs.com/Retech_Project/

판매자 계정
- ID : receiver@naver.com
- PW : asdf1234!

  
구매자 계정
- ID : you1004@gmailll.com
- PW : 1234






## 1. 프로젝트 개요


### 💡서비스 요약
----------------
- 판매자/구매자 간 실시간 1:1 채팅 (웹소켓)

- 계좌와 연결된 자체페이로 [ 충전/환급/수익/지출 ] 관리 (금융결제원 API)

- 거래상품 등록 및 인기/연관 검색어 도출 (Ajax)

- 자체 스토어 상품 구매 및 결제 (포트원 API)



### 📅개발 기간
----------------
2024.07.25 - 진행중

|개발 환경||
|------|---|
|버전 관리|<img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>|
|개발 도구|<img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=Eclipse%20IDE&logoColor=white">|
|개발 언어 및 프레임워크|<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white"/><img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>|
|라이브러리|<img src="https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jQuery&logoColor=white"/><img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white">|
|데이터베이스|<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>|
|협업 도구|<img alt="Slack" src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white" /><img src="https://img.shields.io/badge/Google%20Sheets-34A853?style=for-the-badge&logo=google-sheets&logoColor=white"/>|

### 🙋🏻‍♀️팀원
----------------
|팀장|부팀장|서기|팀원|팀원|
|------|---|---|---|---|
|<div align="center">[옥혜지](https://github.com/devok11)</div>|<div align="center">[박지혁](https://github.com/royalcross)</div>|<div align="center">[진성민](https://github.com/ns0304)</div>|<div align="center">[강연진](https://github.com/yeonjinnk)</div>|<div align="center">[성수안](https://github.com/ssa121600)</div>|
|결제<br>페이|관리자페이지<br>CSS|상품 등록<br>인기/연관검색어|채팅<br>스토어 결제<br>리뷰|로그인<br>회원가입<br>마이페이지<br>CSS|

---------------------------------------------------------------------



## 2. 주요 기능
---------------------------------------------------------------------

|메인페이지|
|------|
|<img src="https://github.com/user-attachments/assets/5fa420ca-3627-4092-8888-a4632c954178"  width="800" height="750"/>|
|- 이미지 슬라이드<br>- 카테고리 상품<br>- 인기 상품<br>- 최근 업데이트 상품|

|로그인|회원가입|
|------|---|
|<img src="https://github.com/user-attachments/assets/f1b86b94-54f0-4a4d-8b08-d6e312053da0"  width="300" height="500"/>|<img src="https://github.com/user-attachments/assets/7822c9c5-a6b6-4288-a6d8-c560a1eb4fe9"  width="400" height="200"/>|
|- 카카오톡 연동 로그인<br>- 아이디, 비밀번호 찾기|- COOLSMS 문자인증 서비스를 통한 본인 인증|


|마이페이지|
|------|
|<img src="https://github.com/user-attachments/assets/aa177e7e-7943-4fbc-a100-2d9bb114727c"  width="800" height="600"/>|
|- 판매내역<br>- 구매내역<br>- 찜한상품<br>- 회원정보 수정|

|스토어 상품|결제|
|------|------|
|<img src="https://github.com/user-attachments/assets/056aa97c-5f95-4f9f-b857-418d2e8286e4"  width="400" height="300"/>|<img src="https://github.com/user-attachments/assets/4024a029-bbab-4c1b-b7ff-70e87f455c5a"  width="400" height="300"/>|
|- 상세 이미지 확대<br>- 수량 변경|- 카카오페이 결제|

|채팅|채팅 알림|
|------|------|
|<img src="https://github.com/user-attachments/assets/da0b6dfc-eaa6-4c1b-b2b1-78f62b7601ac"  width="400" height="300"/>|<img src="https://github.com/user-attachments/assets/fee04af6-5d43-4c19-931e-ab12cc4a37b6"  width="400" height="300"/>|
|- 1:1채팅<br>- 버튼 통해 판매자는 금액 입력 및 구매자는 결제 가능<br>- 채팅 신고하기<br>- 채팅 종료|- 미확인 채팅 알림 메뉴 위 적색 아이콘으로 표시<br>- 수신된 채팅 내용을 알림 메뉴에서 확인 가능|

|최근 검색어|인기 검색어|
|------|------|
|<img src="https://github.com/user-attachments/assets/55c1ee20-209f-4ed6-9cf1-f8307c130cc2"  width="400" height="300"/>|<img src="https://github.com/user-attachments/assets/c720475e-1593-43e5-82f4-31952df56875"  width="400" height="300"/>|
|- 최근 검색어 확인|- 인기 검색어 1~20위 확인|

|상품 목록|상품 상세|
|------|------|
|<img src="https://github.com/user-attachments/assets/12da34f9-a662-44ea-a0b8-d7a1c8be7069"  width="400" height="300"/>|<img src="https://github.com/user-attachments/assets/daabd340-8ef4-4a44-88bd-7d46d993792f"  width="400" height="300"/>|
|- 카테고리 선택 <br>- 아래로 스크롤 시 게시글 자동 로딩|- 상품의 이미지가 슬라이드 형식으로 출력<br>- 거래하기, 찜하기 버튼 및 판매자 정보<br>- 판매자 본인은 수정하기/삭제하기/끌어올리기 가능

