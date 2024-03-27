<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bookstore_style.css">

<title>Trang chủ quản trị</title>
</head>
<body>
	<jsp:include page="_header_backend.jsp"></jsp:include>
	<jsp:include page="_menu_backend.jsp"></jsp:include>
	<div align="center">
		<h3>Chỉnh sửa thông tin sách</h3>
		<p style="color: red">${errors }</p>
		<c:if test="${not empty book}">
			<form action="editBook" method="POST" enctype="multipart/form-data">
				<input type="hidden" name="bookId" value="${book.bookId }" />
				<table>
					<tr>
						<td>Tiêu đề</td>
						<td><input type="text" name="title" value="${book.title }"></td>
					</tr>
					<tr>
						<td>Tác giả</td>
						<td><input type="text" name="author" value="${book.author }"></td>
					</tr>
					<tr>
						<td>Giá tiền</td>

						<td><input type="text" name="price" value="${book.price }">&nbsp;&nbsp;(vđn)
						</td>
					</tr>
					<tr>
						<td>Số lượng có trong kho</td>
						<td><input type="text" name="quantityInStock"
							value="${book.quantityInStock }" required="required"></td>
					</tr>
					<tr>
						<td>Ảnh bìa sách</td>
						<td><img id="bookImage" alt="" src="${book.imagePath }"
							width="150"> <input type="hidden"
							value="${book.imagePath }" name="imagePath" />
							&nbsp; Đổi ảnh: &nbsp;<input type="file" name="file" accept="image/*"
							onchange="loadImage(event)" />
							</td>
					</tr>
					<tr>
						<td>Giới thiệu sách</td>
						<td><textarea name="detail" id="editor" cols="10" rows="20">${book.detail }</textarea>
						<script>
							ClassicEditor.create(document
									.querySelector('#editor'));
						</script></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" value="Cập nhật">
							&nbsp;&nbsp; <a href="adminHome">Bỏ qua</a></td>
					</tr>
				</table>
			</form>
		</c:if>
	</div>
</body>
</html>