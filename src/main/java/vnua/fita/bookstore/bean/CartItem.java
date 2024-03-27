package vnua.fita.bookstore.bean;

public class CartItem {
	//cuon sach chon mua
	private Book selectedBook;
	
	//so luong mua cuon sach do
	private int quantity;
	public CartItem(Book selectedBook, int quantity) {
		super();
		this.selectedBook = selectedBook;
		this.quantity = quantity;
	}
	public Book getSelectedBook() {
		return selectedBook;
	}
	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
