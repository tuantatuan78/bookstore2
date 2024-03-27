package vnua.fita.bookstore.bean;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	// danh sach sach chon dua vao gio hang
	private Map<Integer, CartItem> cartItemList;
	private float totalCost; // tong so tien trong gio hang
	private String paymentMode; // hinh thuc thanh toan
	private boolean paymentStatus; // trang thai thanh toan (true: roi; false: chua)

	public Cart() {
		cartItemList = new HashMap<Integer, CartItem>();
		totalCost = 0;
	}

	// them mat hang vao gio hang, neu trung mat hang thi thay the
	public void addCartItemToCart(int bookId, CartItem cartItem) {
		CartItem oldCartItem = cartItemList.get(bookId);

		// neu trung lap mat hang se thay the o duoi nen bot phan tien cu trong tong so
		// tien gio hang di
		if (oldCartItem != null) {
			totalCost -= oldCartItem.getQuantity()
					* oldCartItem.getSelectedBook().getPrice();
		}
		cartItemList.put(bookId, cartItem); // thay the neu da ton tai

		// cap nhat tong so tien gio hang
		totalCost += cartItem.getQuantity() * cartItem.getSelectedBook().getPrice();
	}

	// xoa mat hang vao gio hang
	public void removeCartItemFromCart(int bookId) {
		CartItem cartItem = cartItemList.get(bookId); // lay cartItem can xoa bang id
		cartItemList.remove(bookId);
		// cap nhat tong so tien gio hang
		totalCost -= cartItem.getQuantity() * cartItem.getSelectedBook().getPrice();
	}

	public float getTotalCost() {
		return totalCost;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public Map<Integer, CartItem> getCartItemList() {
		return cartItemList;
	}

	public void setCartItemList(Map<Integer, CartItem> cartItemList) {
		this.cartItemList = cartItemList;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

}
