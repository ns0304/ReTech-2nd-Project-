<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 목록</title>
    <link href="${pageContext.request.contextPath}/resources/css/default.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/admin_default.css" rel="stylesheet" type="text/css">
    <style>
        .main {
            padding: 1.8rem;
        }
        .main h3 {
            text-align: left;
            margin-bottom: 30px;
        }
        .main .wrapper_top {
            display: flex;
            justify-content: space-between;
            position: relative;
            margin-bottom: 20px;
        }
        .main .wrapper_top .search {
            width: 270px;
            position: absolute;
            left: 40%;
        }
        .main .content {
            width: 100%;
            margin-bottom: 50px;
        }
        .main .content table {
            width: 100%;
        }
        .main .content table th {
            background-color: #eee;
        }
        .main .content table #yAdmin {
            background-color: orange;
        }
        .main #pageList {
            text-align: center;
        }
    </style>
    <script>
        function showListLimit(limit){
            location.href = "AdminMemberList?listLimit=" + limit;
        }
        
        function confirmAdmin(id, isAdmin, isAuthorize){
            let action = (isAuthorize === 'Y') ? "해제" : "부여";
            if(confirm("관리자 권한을 " + action + "하시겠습니까?")){
                location.href = "ChangeAdminAuthorize?member_id=" + id + "&member_isAdmin=" + isAdmin + "&isAuthorize=" + isAuthorize;
            }
        }
    </script>
</head>
<body>
    <header>
        <jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
    </header>  
    <div class="inner">
        <section class="wrapper">
            <jsp:include page="/WEB-INF/views/inc/admin_side_nav.jsp"></jsp:include>
            <article class="main">
                <h3>회원 목록</h3>
                <div class="content">
                    <table border="1">
                        <tr>
                            <th>회원아이디</th>
                            <th>이름</th>
                            <th>상점이름</th>
                            <th>전화번호</th>
                            <th>회원상태</th>
                            <th>관리자여부</th>
                            <th>관리자 권한관리</th>
                        </tr>
                        <c:set var="pageNum" value="1" />
                        <c:if test="${not empty param.pageNum}">
                            <c:set var="pageNum" value="${param.pageNum}" />
                        </c:if>
                        <c:forEach var="member" items="${memberList}">
                            <tr align="center">
                                <td>${member.member_id}</td>
                                <td>${member.member_name}</td>
                                <td>${member.member_nickname}</td>
                                <td>${member.member_phone}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${member.member_status eq '탈퇴'}">
                                            <span class="status-x">X</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-o">O</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${member.member_isAdmin eq 'Y'}">
                                            Y
                                        </c:when>
                                        <c:otherwise>
                                            N
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${member.member_isAdmin eq 'Y'}">
                                            <input type="button" value="관리자 권한 해제" id="yAdmin" onclick="confirmAdmin('${member.member_id}', '${member.member_isAdmin}', 'N')">
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" value="관리자 권한 부여" onclick="confirmAdmin('${member.member_id}', '${member.member_isAdmin}', 'Y')"
                                                <c:if test="${member.member_status eq '탈퇴'}"> disabled</c:if>>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty memberList}">
                            <tr>
                                <td align="center" colspan="7">검색 결과가 없습니다.</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
                <div id="pageList">
                    <input type="button" value="이전" onclick="location.href='AdminMemberList?pageNum=${pageNum - 1}'">
                    <c:forEach var="i" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
                        <c:choose>
                            <c:when test="${i eq pageNum}">
                                <b>${i}</b>
                            </c:when>
                            <c:otherwise>
                                <a href="AdminMemberList?pageNum=${i}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <input type="button" value="다음" onclick="location.href='AdminMemberList?pageNum=${pageNum + 1}'">
                </div>
            </article>
        </section>
    </div>
    <footer>
        <jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
    </footer>
</body>
</html>