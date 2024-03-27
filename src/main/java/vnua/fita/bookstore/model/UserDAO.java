package vnua.fita.bookstore.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vnua.fita.bookstore.bean.User;

public class UserDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	private PreparedStatement preStatement;
	private ResultSet resultSet;

	public UserDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public User findUser(String username, String password) {
		String sql = "SELECT * FROM tbluser WHERE username = ? AND password = ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
//		if (jdbcConnection == null) {
//			jdbcConnection = DBConnection.createConnection(
//					"jdbc:mysql://localhost:3306/bookstore", "root", "123456");
//		}
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setString(1, username);
			preStatement.setString(2, password);
			resultSet = preStatement.executeQuery();
			if (resultSet.next()) {
				return new User(resultSet.getString("username"),
						resultSet.getString("password"), resultSet.getByte("role"),
						resultSet.getString("fullname"), resultSet.getString("email"),
						resultSet.getString("mobile"), resultSet.getString("address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User findUser(String username) {
		String sql = "SELECT * FROM tbluser WHERE username = ?";
		jdbcConnection = DBConnection.createConnection(jdbcURL, jdbcUsername,
				jdbcPassword);
		try {
			preStatement = jdbcConnection.prepareStatement(sql);
			preStatement.setString(1, username);
			resultSet = preStatement.executeQuery();
			if (resultSet.next()) {
				return new User(resultSet.getString("username"),
						resultSet.getString("password"), resultSet.getByte("role"),
						resultSet.getString("fullname"), resultSet.getString("email"),
						resultSet.getString("mobile"), resultSet.getString("address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
