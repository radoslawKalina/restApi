package wrss.rest.models.response;

import org.springframework.hateoas.RepresentationModel;

import wrss.rest.models.request.UserDetailsRequestModel;

public class UserWithDetailsResponseModel extends RepresentationModel {

	private String userId;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private UserDetailsRequestModel details;

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

	public UserDetailsRequestModel getDetails() {
		return details;
	}

	public void setDetails(UserDetailsRequestModel details) {
		this.details = details;
	}

}
