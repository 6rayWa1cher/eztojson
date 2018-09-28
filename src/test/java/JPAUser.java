import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_table")
public class JPAUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	private Long id;

	@Column(nullable = false, unique = true, length = 25)
	private String login;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(nullable = false)
	private LocalDateTime lastLogin;

	public JPAUser() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
}
