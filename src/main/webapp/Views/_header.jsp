<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bookstore_script.js"></script>
<div style="background: #E0E0E0; height: 75px; padding: 15px">
	<div style="float: left">
		<h2>Website Cửa hàng Sách với JSP/Servlet</h2>
	</div>
	<div style="float: right; text-align: right;">
		<c:if test="${not empty loginedUser }">
			Xin chào: <b> ${loginedUser.fullname} </b>
			|
			<a href="${pageContext.request.contextPath}/customerOrderList">Thông
				tin tài khoản/ đơn hàng</a>
			|
			<a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
		</c:if>

		<c:if test="${empty loginedUser }">
			<a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
		</c:if>

		<c:if test="${not empty cartOfCustomer }">
			<a href="${pageContext.request.contextPath}/cartBook/viewCart"> <img
				alt="ShoppingCart"
				src="${pageContext.request.contextPath}/img/cart.png"
				width="20">
			</a>
		</c:if>
		<br>
		<br> Tìm sách &nbsp; <input name="Search"
			onchange="activeAsLink('${pageContext.request.contextPath}/clientHome?keyword='+this.value)">
	</div>
</div>