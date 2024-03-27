package vnua.fita.bookstore.bean;

public class User {
	private String username;
	private String password;
	private byte role;
	private String fullname;
	private String email;
	private String mobile;
	private String address;

	public User() {
		super();
	}


	public User(String username, String password, byte role, String fullname) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
	}
	

	public User(String username, String password, byte role, String fullname, String email,
			String mobile, String address) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
		this.email = email;
		this.mobile = mobile;
		this.address = address;
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

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

}
