package wrss.rest.dto;

public class UserDetailsDto {

	private int id;
	private String city;
	private String street;
	private String phoneNumber;
	private UserDto userDto;
	
	private String authenticationEmail; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
