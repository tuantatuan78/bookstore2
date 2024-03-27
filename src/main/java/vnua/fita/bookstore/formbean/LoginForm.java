package vnua.fita.bookstore.formbean;

import java.util.ArrayList;
import java.util.List;

public class LoginForm {
	private String username;
	private String password;
	private boolean rememberMe;

	public LoginForm(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> validate() {
		List<String> errors = new ArrayList<String>();
		if (username == null || username.trim().isEmpty()) {
			errors.add("Nhap username");
		}
		if (password == null || password.trim().isEmpty()) {
			errors.add("Nhap password");
		}
		return errors;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}
