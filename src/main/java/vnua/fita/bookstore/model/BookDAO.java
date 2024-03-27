package vnua.fita.bookstore.model;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vnua.fita.bookstore.bean.Book;
import vnua.fita.bookstore.util.Constant;
import vnua.fita.bookstore.util.MyUtil;

public class BookDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	private Statement statement;
	private PreparedStatement preStatement;
	private ResultSet resultSet;

	public BookDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		super();
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public List<Book> listAllBooks() {
		// danh sach chua ket qua tra ve
		List<Book> listBook = new ArrayList<Book>();

		// cau lenh sql
		String sql = "SELECT * FROM	tblbook";

		// tao ket noi
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			// tao doi tuong truy van CSDL
			statement = jdbcConnection.createStatement();

			// thuc hien truy van
			resultSet = statement.executeQuery(sql);

			// duyet qua danh sach ban ghi ket qua tra ve
			while (resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");

				// đóng gói các giá trị thuộc tính vào đối tượng Bean(Book)
				Book book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);

				// Thêm đối tượng Bean vào danh sách
				listBook.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closeStatement(statement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return listBook;
	}

	public List<Book> listAllBooks(String keyword) {
		List<Book> searchBookList = new ArrayList<Book>();

		String sql = "SELECT * FROM tblbook WHERE title LIKE ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setString(1, "%" + keyword + "%");
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				Book book = new Book(id, title, author, price);
				searchBookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return searchBookList;
	}

	public List<Book> listAllBooks(String fromDate, String toDate) {
		List<Book> listBooks = new ArrayList<Book>();
		String sql = "SELECT b.*, sum(obor.quantity) AS sum_quantity, sum(obor.price*obor.quantity) AS sum_price FROM tblbook b "
				+ "LEFT JOIN "
				+ "(SELECT ob.* from tblorder_book ob INNER JOIN tblorder o ON ob.order_id = o.order_id "
				+ "WHERE o.order_status = ? AND (o.status_date BETWEEN ? AND ?)) obor "
				+ "ON b.book_id = obor.book_id "
				+ "GROUP BY b.book_id "
				+ "ORDER BY sum_quantity DESC, b.create_date DESC";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setInt(1, Constant.DELEVERED_ORDER_STATUS);
			preStatement.setString(2, fromDate);
			preStatement.setString(3, toDate);
			resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");
				Date createDate = resultSet.getTimestamp("create_date");
				int soldQuantity = resultSet.getInt("sum_quantity");
				int sumOfSoldBook = resultSet.getInt("sum_price");
				Book book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);
				book.setCreateDate(createDate);
				book.setSoldQuantity(soldQuantity);
				book.setSumOfSoldBook(sumOfSoldBook);
				listBooks.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closeStatement(statement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return listBooks;
	}
	
	public List<Book> listAllBooks(String keyword, String fromDate, String toDate){
		List<Book> searchBookList = new ArrayList<Book>();
		String sql = "SELECT b.*, sum(obor.quantity) AS sum_quantity, sum(obor.price*obor.quantity) AS sum_price FROM tblbook b "
				+ "LEFT JOIN "
				+ "(SELECT ob.* from tblorder_book ob INNER JOIN tblorder o ON ob.order_id = o.order_id "
				+ "WHERE o.order_status = ? AND (o.status_date BETWEEN ? AND ?)) obor "
				+ "ON b.book_id = obor.book_id "
				+ "WHERE title LIKE ? "
				+ "GROUP BY b.book_id "
				+ "ORDER BY sum_quantity DESC, b.create_date DESC";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setInt(1, Constant.DELEVERED_ORDER_STATUS);
			preStatement.setString(2, fromDate);
			preStatement.setString(3, toDate);
			preStatement.setString(4, "%"+keyword+"%");
			resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");
				Date createDate = resultSet.getTimestamp("create_date");
				int soldQuantity = resultSet.getInt("sum_quantity");
				int sumOfSoldBook = resultSet.getInt("sum_price");
				Book book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);
				book.setCreateDate(createDate);
				book.setSoldQuantity(soldQuantity);
				book.setSumOfSoldBook(sumOfSoldBook);
				searchBookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return searchBookList;
	}

	public boolean deleteBook(int bookId) {
		boolean result = false;
		// Cau lenh sql
		String sql = "DELETE FROM tblbook WHERE book_id = ?";

		// Tao ket noi
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);

		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setInt(1, bookId);
			int check = preStatement.executeUpdate();
			if (check > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return result;
	}

	public Book getBook(int id) {
		Book book = null;
		String sql = "SELECT * FROM tblbook WHERE book_id = ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setInt(1, id);
			resultSet = preStatement.executeQuery();
			if (resultSet.next()) {
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");

				// đóng gói các giá trị thuộc tính vào đối tượng Bean(Book)
				book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return book;
	}

	public boolean updateBook(Book book) {
		boolean result = false;
		String sql = "UPDATE tblbook SET title = ?, author = ?, price = ?, quantity_in_stock = ?, "
				+ "detail = ?, image_path = ? WHERE book_id = ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);

		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setString(1, book.getTitle());
			preStatement.setString(2, book.getAuthor());
			preStatement.setInt(3, book.getPrice());
			preStatement.setInt(4, book.getQuantityInStock());
			preStatement.setString(5, book.getDetail());
			preStatement.setString(6, book.getImagePath());
			preStatement.setInt(7, book.getBookId());
			result = preStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}

		return result;
	}

	public boolean insertBook(Book book) {
		boolean insertResult = false;
		String sql = "INSERT INTO tblbook(title, author, price, quantity_in_stock, detail, image_path, create_date) VALUE (?,?,?,?,?,?,?)";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setString(1, book.getTitle());
			preStatement.setString(2, book.getAuthor());
			preStatement.setInt(3, book.getPrice());
			preStatement.setInt(4, book.getQuantityInStock());
			preStatement.setString(5, book.getDetail());
			preStatement.setString(6, book.getImagePath());
			preStatement.setString(7, MyUtil.convertDateToString(book.getCreateDate()));
			insertResult = preStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return insertResult;
	}

	public List<Book> listAllBooks(int offset, int noOfRecords, String keyword) {
		List<Book> listBook = new ArrayList<Book>();
		String sql = "SELECT * FROM tblbook ";
		if (keyword != null && !keyword.isEmpty()) {
			sql += "WHERE title LIKE ? ";
		}
		sql += "ORDER BY create_date DESC ";
		sql += "LIMIT ?, ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			int index = 0;
			preStatement = jdbcConnection.prepareStatement(sql);
			if (keyword != null && !keyword.isEmpty()) {
				preStatement.setString(++index, "%" + keyword + "%");
			}
			preStatement.setInt(++index, offset); // vị trí bắt đầu lấy
			preStatement.setInt(++index, noOfRecords); // số bản ghi lấy ra

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");

				Book book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);
				listBook.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return listBook;
	}
	
	public List<Book> listAllBooks(int offset, int noOfRecords, String keyword,String fromDate,String toDate) {
		List<Book> listBook = new ArrayList<Book>();
		String sql = "SELECT b.*, \r\n"
				+ "       SUM(obor.quantity) AS sum_quantity, \r\n"
				+ "       SUM(obor.price*obor.quantity) AS sum_price \r\n"
				+ "FROM tblbook b \r\n"
				+ "LEFT JOIN (\r\n"
				+ "    SELECT ob.* \r\n"
				+ "    FROM tblorder_book ob \r\n"
				+ "    INNER JOIN tblorder o ON ob.order_id = o.order_id\r\n"
				+ "    WHERE o.order_status = ? AND (o.status_date BETWEEN ? AND ?)\r\n"
				+ ") obor ON b.book_id = obor.book_id \r\n";
				
		if (keyword != null && !keyword.isEmpty()) {
			sql += "WHERE title LIKE ? ";
		}
		sql+= " GROUP BY b.book_id "
				+ " ORDER BY sum_quantity DESC, b.create_date DESC";
		sql += " LIMIT ?, ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			int index = 0;
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setInt(++index, Constant.DELEVERED_ORDER_STATUS);
			preStatement.setString(++index, fromDate);
			preStatement.setString(++index, toDate);
			
			if (keyword != null && !keyword.isEmpty()) {
				preStatement.setString(++index, "%" + keyword + "%");
			}
			preStatement.setInt(++index, offset); // vị trí bắt đầu lấy
			preStatement.setInt(++index, noOfRecords); // số bản ghi lấy ra

			resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				int price = resultSet.getInt("price");
				int quantityInStock = resultSet.getInt("quantity_in_stock");
				String detail = resultSet.getString("detail");
				String imagePath = resultSet.getString("image_path");
				Date createDate = resultSet.getTimestamp("create_date");
				int soldQuantity = resultSet.getInt("sum_quantity");
				int sumOfSoldBook = resultSet.getInt("sum_price");
				Book book = new Book(id, title, author, price, quantityInStock);
				book.setDetail(detail);
				book.setImagePath(imagePath);
				book.setCreateDate(createDate);
				book.setSoldQuantity(soldQuantity);
				book.setSumOfSoldBook(sumOfSoldBook);
				listBook.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return listBook;
	}

	public int getNoOfRecords(String keyword) {
		String sql = "SELECT count(book_id) FROM tblbook ";
		int result = 0;
		if (keyword != null && !keyword.isEmpty()) {
			sql += "WHERE title LIKE ? ";
		}
		sql += "ORDER BY create_date DESC ";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			if (keyword != null && !keyword.isEmpty()) {
				preStatement.setString(1, "%" + keyword + "%");
			}

			resultSet = preStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(resultSet);
			DBConnection.closePreparedStatement(preStatement);
			DBConnection.closeConnect(jdbcConnection);
		}
		return result;
	}
}