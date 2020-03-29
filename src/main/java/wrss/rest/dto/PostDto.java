package wrss.rest.dto;

public class PostDto {

	private int id;
	private String title;
	private String message;
	private UserDto userDto;
	
	private String authenticationEmail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getAuthenticationEmail() {
		return authenticationEmail;
	}

	public void setAuthenticationEmail(String authenticationEmail) {
		this.authenticationEmail = authenticationEmail;
	}
	
	

}
