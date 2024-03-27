<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bookstore_style.css">
<title>Trang chủ phía admin</title>
</head>
<body>
	<jsp:include page="_header_backend.jsp"></jsp:include>
	<jsp:include page="_menu_backend.jsp"></jsp:include>
	<div align="center">
		<h3>Danh sách các cuốn sách</h3>
		<p style="color: red">${errors }</p>
		<form action="deleteBook" id="deleteBookFromAdminForm" method="post">
			<input type="hidden" name="bookId" id="deleteBookFromAdmin" />
		</form>
		<div style="margin-bottom: 10px;">
			<form method="post" action="adminHome">
			<input value="${keyword }" name="keyword" hidden/>
			<input value="${currentPage }" name="page" hidden/>
				<b>Lọc theo ngày bán: </b> Từ ngày &nbsp; <input type="date"
					value="${fromDate }" required="required" name="fromDate" />&nbsp;&nbsp;
				tới &nbsp; <input type="date" value="${toDate }" required="required"
					name="toDate" />&nbsp; <input type="submit" value="Lọc" />&nbsp;&nbsp;&nbsp;
				<b>Doanh thu: </b>
				<fmt:formatNumber type="number" maxFractionDigits="0"
					value="${turnover }" />
				<sup>đ</sup>
			</form>
		</div>
		<table border="1">
			<tr>
				<th>Tiêu đề</th>
				<th>Tác giả</th>
				<th>Giá tiền</th>
				<th>Số lượng còn</th>
				<th>Số lượng bán</th>
				<th>Ngày tạo</th>
				<th colspan="3" width="150px">Thao tác</th>
			</tr>
			<c:forEach items="${bookList}" var="book">
				<tr>
					<td>${book.title}</td>
					<td>${book.author}</td>
					<td><fmt:setLocale value="en_US" /> <fmt:formatNumber
							type="number" maxFractionDigits="0" value="${book.price }" /><sup>đ</sup></td>
					<td align="center"><fmt:formatNumber type="number"
							maxFractionDigits="0" value="${book.quantityInStock }" /></td>
					<td align="center"><fmt:formatNumber type="number"
							maxFractionDigits="0" value="${book.soldQuantity }" /></td>
					<td><fmt:formatDate value="${book.createDate }"
							pattern="dd-MM-yyyy HH:mm" /></td>
					<td align="center"><button type="button"
							onclick="activeAsLink('editBook?bookId=${book.bookId}')">Sửa</button></td>
					<td align="center"><button type="button"
							onclick="onClickDeleteBook('${book.title}', ${book.bookId})">Xóa</button></td>
					<td align="center"><button type="button"
							onclick="activeAsLink('detailBook_backend?bookId=${book.bookId}')">Xem
							chi tiết</button></td>
				</tr>
			</c:forEach>
		</table>
		<c:if test="${empty keyword }">
			<div style="margin-top: 5px">
				<!-- link previous chỉ xuất hiện khi trang hiện tại lớn hơn 1 -->
				<c:if test="${currentPage gt 1 }">
					<a href="adminHome?page=${currentPage - 1} ">Previous</a> &nbsp;
				</c:if>
				<c:forEach begin="1" end="${noOfPages }" var="i">
					<c:choose>
						<c:when test="${currentPage eq i}"> <!-- Trùng lặp trang hiện tại thì không tạo link -->
							&nbsp;${i}&nbsp;
						</c:when>
						<c:otherwise>
							&nbsp;<a href="adminHome?page=${i}">${i}</a>&nbsp;
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<!-- Link Next chỉ xuất hiện khi trang hiện tại nhỏ hơn tổng số trang -->
				<c:if test="${currentPage lt noOfPages }">
					&nbsp;<a href="adminHome?page=${currentPage + 1}">Next</a>
				</c:if>
			</div>
		</c:if>

		<!-- có hoạt động tìm kiếm, thêm tham số keyword -->
		<c:if test="${not empty keyword }">
			<div style="margin-top: 5px">
				<c:if test="${currentPage gt 1}">
					<a href="adminHome?page=${currentPage - 1}&keyword=${keyword}">Previous</a>&nbsp;
				</c:if>
				<c:forEach begin="1" end="${noOfPages }" var="i">
					<c:choose>
						<c:when test="${currentPage eq i}">
							&nbsp;${i}&nbsp;
						</c:when>
						<c:otherwise>
							&nbsp;<a href="adminHome?page=${i}&keyword=${keyword}">${i}</a>&nbsp;
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${currentPage lt noOfPages }">
					&nbsp;<a
						href="adminHome?page=${currentPage + 1}&keyword=${keyword}">Next</a>
				</c:if>
			</div>
		</c:if>
		<br>
		<sup>(* Cột số lượng bán: Mặc định hiển thị số sách bán trong 12 tháng cho đến ngày hiện tại *)</sup>
		
		<br/><a href="createBook">Thêm sách mới</a><br>
	</div>
</body>
</html>