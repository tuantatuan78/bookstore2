var request;
function activeAsLink(link) {
	window.location = link;
}

function plusValue(elementId, maxQuantity) {
	let quantity = parseInt(document.getElementById(elementId).value);
	if (quantity + 1 <= maxQuantity) {
		document.getElementById(elementId).value = quantity + 1;
	} else {
		alert('Giá trị không được vượt quá: ' + maxQuantity);
	}
}

function minusValue(elementId) {
	let quantity = parseInt(document.getElementById(elementId).value);
	if (quantity - 1 >= 1) {
		document.getElementById(elementId).value = quantity - 1;
	}
}

function validateValue(element, maxQuantity) {
	if (element.value > maxQuantity) {
		alert('Giá trị không được vượt quá: ' + maxQuantity);
	}
}

function checkQuantityAndSubmit(elementId, bookId, maxQuantity) {
	let quantity = parseInt(document.getElementById(elementId).value);
	if (quantity <= 0) {
		alert('Nhập số lượng!');
		return;
	} else if (quantity > maxQuantity) {
		alert('Giá trị không được vượt quá: ' + maxQuantity);
		return;
	} else {
		document.getElementById("detailBookForm").submit();
	}
}

function updateQuantityOfCartItem(newQuantity, bookId) {
	//url nay se goi den servlet hien tai(CartServlet) voi tham so kem theo
	var url = 'addToCart?bookId=' + bookId + '&quantityPurchased=' + newQuantity;
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP")
	}
	try {
		request.onreadystatechange = getInfo;
		request.open("GET", url, true);
		request.send();
	} catch (e) {
		alert("Unable to connect to server");
	}
}

function getInfo() {
	if (request.readyState == 4) {
		var val = request.responseText;
	}
}

//ham dc goi ung voi su kien onchange, khi so luong mua thay doi
function validateValueAndUpdateCart(element, maxQuantity, bookId, price) {
	var newQuantity = element.value;
	if (newQuantity > maxQuantity) {
		alert('Giá trị không được vượt quá ' + maxQuantity);
	} else if (newQuantity > 0) {
		//ajax gửi request đến server để update cart
		updateQuantityOfCartItem(newQuantity, bookId);

		//update subtotal
		document.getElementById("subtotal" + bookId).innerText = toComma(newQuantity * price);
		let subtotalList = document.querySelectorAll('[id^="subtotal"]');
		let total = 0;
		for (let i = 0; i < subtotalList.length; i++) {
			total += parseInt(subtotalList[i].innerText.replace(/,/g, ""));
		}
		document.getElementById("total").innerText = toComma(total);
	}
}

//định dạng số tiền: phân cách hàng nghìn bởi dấu phẩy
function toComma(n) {
	return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function minusValueAndUpdateCart(elementId) {
	var quantity = parseInt(document.getElementById(elementId).value);
	if (quantity - 1 >= 1) {
		document.getElementById(elementId).value = quantity - 1;
		//hàm validate được gọi ứng với sự kiện onchange
		document.getElementById(elementId).onchange();
	}
}

function plusValueAndUpdateCart(elementId, maxQuantity) {
	var quantity = parseInt(document.getElementById(elementId).value);
	if (quantity + 1 <= maxQuantity) {
		document.getElementById(elementId).value = quantity + 1;
		//hàm validate được gọi ứng với sự kiện onchange
		document.getElementById(elementId).onchange();
	} else {
		alert('Giá trị không được vượt quá ' + maxQuantity);
	}
}

function onClickRemoveBook(bookTitle, bookId) {
	let c = confirm('Bạn chắc chắn muốn xóa cuốn sách ' + bookTitle + ' khỏi giỏ hàng');
	if (c) {
		document.getElementById("removedBookFromCart").value = bookId;
		document.getElementById("removedBookFromCartForm").submit();
	}
}

function loadImage(event) {
	let output = document.getElementById('bookImage');
	output.src = URL.createObjectURL(event.target.files[0]);
	output.onload = function() {
		URL.revokeObjectURL(output.src)
	}
}
function onClickDeleteBook(bookTitle, bookId) {
	let c = confirm('Bạn chắc chắn muốn xóa cuốn sách ' + bookTitle + '?');
	if (c) {
		document.getElementById("deleteBookFromAdmin").value = bookId;
		document.getElementById("deleteBookFromAdminForm").submit();
	}
}
function onClickAdminOrderConfirm(orderId,confirmType,action){
	document.getElementById("orderIdOfAction").value=orderId;
	document.getElementById("confirmTypeOfAction").value=confirmType;
	document.getElementById("adminOrderForm").action=action.substring(0);
	document.getElementById("adminOrderForm").submit();
}
