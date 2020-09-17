package ksnu.cie.hw;

public abstract class User {
	private String id;
	private String password;
	private String name;
	private String address;
	private String grade;
	private int type;
	
	public User(String id, String password, String name, String address, String grade, int type) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.address = address;
		this.grade = grade;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
	
}
