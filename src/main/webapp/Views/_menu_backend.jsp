<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div style="padding: 5px; text-align: center;">
    <a href="${pageContext.request.contextPath}/adminHome" id="homeLink">Trang chủ</a>
    |
    <a href="${pageContext.request.contextPath}/adminOrderList/waiting" id="waitingLink">Các đơn hàng chưa xác nhận</a>
    |
    <a href="${pageContext.request.contextPath}/adminOrderList/delivering" id="deliveringLink">Các đơn hàng đang chờ giao</a>
    |
    <a href="${pageContext.request.contextPath}/adminOrderList/delivered" id="deliveredLink">Các đơn hàng đã giao</a>
    |
    <a href="${pageContext.request.contextPath}/adminOrderList/reject" id="rejectLink">Các đơn hàng khách trả lại</a>
</div>

<script>
    // Lấy URL của trang hiện tại
    var currentPageUrl = window.location.href;

    // Lấy các liên kết từ DOM
    var homeLink = document.getElementById('homeLink');
    var waitingLink = document.getElementById('waitingLink');
    var deliveringLink = document.getElementById('deliveringLink');
    var deliveredLink = document.getElementById('deliveredLink');
    var rejectLink = document.getElementById('rejectLink');

    // Xác định và thay đổi màu sắc của liên kết tương ứng với trang hiện tại
    if (currentPageUrl.includes("/adminHome")) {
        homeLink.style.color = "blue";
    } else if (currentPageUrl.includes("/adminOrderList/waiting")) {
        waitingLink.style.color = "blue";
    } else if (currentPageUrl.includes("/adminOrderList/delivering")) {
        deliveringLink.style.color = "blue";
    } else if (currentPageUrl.includes("/adminOrderList/delivered")) {
        deliveredLink.style.color = "blue";
    } else if (currentPageUrl.includes("/adminOrderList/reject")) {
        rejectLink.style.color = "blue";
    }
</script>
