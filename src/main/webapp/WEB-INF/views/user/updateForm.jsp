<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<div class="container">
    <form>
        <input type="hidden" id="id" value="${principal.user.id}">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text"  value="${principal.user.username}" class="form-control" placeholder="Enter username" id="username" readonly>
        </div>

        <!-- 카카오로 로그인 한사람들은 아무것도 수정 못하게 -->
        <c:if test="${empty principal.user.oauth}">
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" placeholder="Enter password" id="password">
        </div>

        </c:if>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email"value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
        </div>


    </form>
        <button id="btn-update" class="btn btn-primary">회원수정완료</button>
</div>

<br>
<!-- 정적 파일은 static이 기본 경로 -->
<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>
