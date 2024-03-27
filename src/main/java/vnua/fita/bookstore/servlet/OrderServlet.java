package vnua.fita.bookstore.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.JsonObject;

import vnua.fita.bookstore.bean.Cart;
import vnua.fita.bookstore.bean.CartItem;
import vnua.fita.bookstore.bean.Order;
import vnua.fita.bookstore.bean.User;
import vnua.fita.bookstore.config.Config;

import vnua.fita.bookstore.model.BookDAO;
import vnua.fita.bookstore.model.OrderDAO;
import vnua.fita.bookstore.util.Constant;
import vnua.fita.bookstore.util.MyUtil;
import com.google.gson.JsonObject;
/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/order")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024
		* 10, maxRequestSize = 1024 * 1024 * 20)
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderDAO orderDAO;

	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
//		bookDAO = new BookDAO("jdbc:mysql://localhost:3306/bookstore", "root", "123456");
		orderDAO = new OrderDAO(jdbcURL, jdbcUsername, jdbcPassword);
	}

	public OrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Lấy thông tin đơn hàng từ session
	    Order order = (Order) request.getSession().getAttribute("order_vnpay");

	    // Thực hiện việc lưu thông tin đơn hàng vào cơ sở dữ liệu
	    orderDAO.insertOrder(order);

	    // Chuyển hướng đến trang hiển thị thông tin chi tiết đơn hàng
	    request.setAttribute("orderOfCustomer", order);
	    request.getRequestDispatcher("Views/detailOrderView.jsp").forward(request, response);

	    
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<String> errors = new ArrayList<String>();
		String deliveryAddress = req.getParameter("deliveryAddress");
		String paymentMode = req.getParameter("paymentMode");
		Part filePart = req.getPart("file");

		HttpSession session = req.getSession();

		validateOrderForm(deliveryAddress, paymentMode, filePart, errors);
		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/Views/cartView.jsp");
			dispatcher.forward(req, resp);
			return;
		}
		if (Constant.CASH_PAYMENT_MODE.equals(paymentMode) || Constant.TRANSFER_PAYMENT_MODE.equals(paymentMode)) {
			Order order = createOrder(deliveryAddress, paymentMode, filePart, session);
			String fowardPage;
			if (orderDAO.checkAndUpdateAvaiableBookOfOrder(order)) {
				boolean insertResult = orderDAO.insertOrder(order);
				if (insertResult) {
					req.setAttribute(Constant.CART_OF_CUSTOMER, MyUtil.getCartOfCustomer(session));
					req.setAttribute(Constant.ORDER_OF_CUSTOMER, order);
					MyUtil.deleteCart(session);
					fowardPage = "/Views/detailOrderView.jsp";
				} else {
					req.setAttribute("errors", Constant.ADD_TO_CART_ACTION);
					fowardPage = "/Views/cartView.jsp";
				}
			} else {
				MyUtil.updateCartOfCustomer(session, converListToMap(order.getOrderBookList()));
				fowardPage = "/Views/cartView.jsp";
			}
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(fowardPage);
			dispatcher.forward(req, resp);
		} else if (Constant.VNPAY_PAYMENT_MODE.equals(paymentMode)) {
			String vnp_Version = "2.1.0";
			String vnp_Command = "pay";
			String orderType = "other";
			String amout_str = req.getParameter("amount");
			String[] parts = amout_str.split("\\."); // Sử dụng \\ để tránh ký tự đặc biệt trong regex
			int amount = Integer.parseInt(parts[0]) * 100;
			String bankCode = req.getParameter("bankCode");

			String vnp_TxnRef = Config.getRandomNumber(8);
			String vnp_IpAddr = Config.getIpAddress(req);

			String vnp_TmnCode = Config.vnp_TmnCode;

			Map<String, String> vnp_Params = new HashMap<>();
			vnp_Params.put("vnp_Version", vnp_Version);
			vnp_Params.put("vnp_Command", vnp_Command);
			vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
			vnp_Params.put("vnp_Amount", String.valueOf(amount));
			vnp_Params.put("vnp_CurrCode", "VND");

			if (bankCode != null && !bankCode.isEmpty()) {
				vnp_Params.put("vnp_BankCode", bankCode);
			}
			vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
			vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
			vnp_Params.put("vnp_OrderType", orderType);

			String locate = req.getParameter("language");
			if (locate != null && !locate.isEmpty()) {
				vnp_Params.put("vnp_Locale", locate);
			} else {
				vnp_Params.put("vnp_Locale", "vn");
			}
			vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
			vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

			Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String vnp_CreateDate = formatter.format(cld.getTime());
			vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

			cld.add(Calendar.MINUTE, 15);
			String vnp_ExpireDate = formatter.format(cld.getTime());
			vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

			List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
			Collections.sort(fieldNames);
			StringBuilder hashData = new StringBuilder();
			StringBuilder query = new StringBuilder();
			Iterator<String> itr = fieldNames.iterator();
			while (itr.hasNext()) {
				String fieldName = (String) itr.next();
				String fieldValue = (String) vnp_Params.get(fieldName);
				if ((fieldValue != null) && (fieldValue.length() > 0)) {
					// Build hash data
					hashData.append(fieldName);
					hashData.append('=');
					hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					// Build query
					query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
					query.append('=');
					query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					if (itr.hasNext()) {
						query.append('&');
						hashData.append('&');
					}
				}
			}
			String queryUrl = query.toString();
			String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
			queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
			String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
			Order order = createOrder(deliveryAddress, paymentMode, filePart, session);
			session.setAttribute("order_vnpay", order);
			resp.sendRedirect(paymentUrl);

		}
	}

	private Order createOrder(String deliveryAddress, String paymentMode, Part filePart, HttpSession session)
			throws IOException {
		Order order = new Order();
		Date date = new Date();

		Cart cartOfCustomer = MyUtil.getCartOfCustomer(session);
		String customerName = MyUtil.getLoginedUser(session).getUsername();
		User customer = new User();
		customer.setUsername(customerName);
		order.setCustomer(customer);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentMode(paymentMode);
		order.setOrderDate(date);
		order.setStatusDate(date);
		order.setTotalCost((int) cartOfCustomer.getTotalCost());
		order.setOrderBookList(new ArrayList<CartItem>(cartOfCustomer.getCartItemList().values()));
		if (Constant.CASH_PAYMENT_MODE.equals(paymentMode)) {
			order.setOrderStatus(Constant.DELEVERING_ORDER_STATUS);
			order.setOrderApproveDate(date);
			order.setPaymentStatus(false);
		} else if (Constant.TRANSFER_PAYMENT_MODE.equals(paymentMode)) {
			String fileName = customerName + "_" + MyUtil.getTimeLabel() + MyUtil.extracFileExtension(filePart);
			String contextPath = getServletContext().getRealPath("/"); // Lấy đường dẫn thực của ứng dụng web
			String savePath = contextPath + "transfer-img-upload"; // Đường dẫn đến thư mục 'img'

			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
			    fileSaveDir.mkdir(); // Tạo thư mục 'img' nếu nó không tồn tại
			}
			order.setOrderApproveDate(date);

			String filePath = savePath + File.separator + fileName; // Đường dẫn file cuối cùng để lưu trữ ảnh
			filePart.write(filePath); // Lưu file ảnh
			String imagePath = "transfer-img-upload" + File.separator + fileName; 
			order.setOrderStatus(Constant.WAITING_CONFIRM_ORDER_STATUS);
			order.setPaymentStatus(false);
			order.setPaymentImagePath(imagePath);
		}else if(Constant.VNPAY_PAYMENT_MODE.equals(paymentMode)) {
			order.setOrderStatus(Constant.DELEVERING_ORDER_STATUS);
			order.setOrderApproveDate(date);
			order.setPaymentStatus(true);
		}
		return order;
	}
	private Map<Integer, CartItem> converListToMap(List<CartItem> orderBookList) {
		Map<Integer, CartItem> cartItemList = new HashMap<Integer, CartItem>();
		for (CartItem cartItem : orderBookList) {
			cartItemList.put(cartItem.getSelectedBook().getBookId(), cartItem);
		}
		return cartItemList;
	}
	private void validateOrderForm(String deliveryAddress, String paymentMode, Part filePart, List<String> errors) {
		if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
			errors.add(Constant.DELEVERY_ADDRESS_EMPTY_VALIDATE_MSG);
		}
		if (Constant.TRANSFER_PAYMENT_MODE.equals(paymentMode)) {
			if (filePart == null || filePart.getSize() < 0) {
				errors.add(Constant.TRANSFER_IMAGE_EMPTY_MSG);
			}
		}
	}
}
