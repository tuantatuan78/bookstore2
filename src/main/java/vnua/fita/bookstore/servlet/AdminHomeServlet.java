package vnua.fita.bookstore.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnua.fita.bookstore.bean.Book;
import vnua.fita.bookstore.model.BookDAO;
import vnua.fita.bookstore.util.MyUtil;

public class AdminHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO;

	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
//		bookDAO = new BookDAO("jdbc:mysql://localhost:3306/bookstore", "root", "123456");
		bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String errors = null;
		List<Book> list = null;
		String fromDate = null;
		String toDate = null;
		int page = 1;
		int recordsPerPage = 2;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		String keyword = request.getParameter("keyword");
		
		list = bookDAO.listAllBooks((page - 1) * recordsPerPage, recordsPerPage, keyword);
		int noOfRecords = bookDAO.getNoOfRecords(keyword);
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		String fromDateParam = request.getParameter("fromDate");
		String toDateParam = request.getParameter("toDate");
		if (validateDate(fromDateParam, toDateParam)) {
			fromDate = MyUtil.attachTailToDate(fromDateParam);
			toDate = MyUtil.attachTailToDate(toDateParam);
		}else {
			Date today = new Date();
			Date todaySubtract12Month = MyUtil.subtractFromDate(12, today);
			fromDate = MyUtil.convertDateToString(todaySubtract12Month);
			toDate = MyUtil.convertDateToString(today);
		}
		
		list = bookDAO.listAllBooks((page - 1) * recordsPerPage, recordsPerPage, keyword,fromDate,toDate);
		if (list.isEmpty()) {
			errors = "Không có cuốn sách nào";
		}
		
		request.setAttribute("errors", errors);
		request.setAttribute("turnover", calSumOfMoney(list));
		request.setAttribute("bookList", list);
		request.setAttribute("noOfPages", noOfPages);
		request.setAttribute("currentPage", page);
		request.setAttribute("keyword", keyword);
		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/Views/adminHomeView.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String errors = null;
		String forwardPage;
		List<Book> list = null;

		String fromDateParam = request.getParameter("fromDate");
		String toDateParam = request.getParameter("toDate");
		if (validateDate(fromDateParam, toDateParam)) {
			String fromDate = MyUtil.attachTailToDate(fromDateParam);
			String toDate = MyUtil.attachTailToDate(toDateParam);
			list = bookDAO.listAllBooks(fromDate, toDate);
			if (list.isEmpty()) {
				errors = "Không có cuốn sách nào";
			}

			request.setAttribute("errors", errors);
			request.setAttribute("fromDate", fromDateParam);
			request.setAttribute("toDate", toDateParam);
			request.setAttribute("turnover", calSumOfMoney(list));
			request.setAttribute("bookList", list);
			forwardPage = "/Views/adminHomeView.jsp";
		} else {
			forwardPage = "adminHome";
		}
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(forwardPage);
		rd.forward(request, response);
		doGet(request, response);
	}

	private boolean validateDate(String fromDate, String toDate) {
		if (fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
			return true;
		}
		return false;
	}

	private int calSumOfMoney(List<Book> list) {
		int sum = 0;
		for (Book book : list) {
			sum += book.getSumOfSoldBook();
		}
		return sum;
	}
}