package vnua.fita.tthieu.springboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String fullname;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;

	private String password;
	
	@Column(nullable = false, unique = true, length = 11)
	private String phone;
	
	@Column(nullable = false, length = 200)
	private String adress;
	private String role; // "user" hoặc "admin"

	public User() {
		// TODO Auto-generated constructor stub
	}
	public User(String fullname, String email, String password, String phone, String role) {
		super();
		this.fullname = fullname;
		this.email = email;
		this.password = password;
		this.phone = phone;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}

	// Getter, Setter
}
