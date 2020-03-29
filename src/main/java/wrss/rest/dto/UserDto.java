package wrss.rest.dto;

import java.io.Serializable;
import java.util.List;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 87298117333730889L;

	private int id;
	private String userId;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private UserDetailsDto userDetails;
	private List<PostDto> posts;
	
	private String authenticationEmail; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public UserDetailsDto getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetailsDto userDetails) {
		this.userDetails = userDetails;
	}

	public List<PostDto> getPosts() {
		return posts;
	}

	public void setPosts(List<PostDto> posts) {
		this.posts = posts;
	}

	public String getAuthenticationEmail() {
		return authenticationEmail;
	}

	public void setAuthenticationEmail(String authenticationEmail) {
		this.authenticationEmail = authenticationEmail;
	}

}
