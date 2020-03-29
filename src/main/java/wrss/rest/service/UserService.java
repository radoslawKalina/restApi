package wrss.rest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import wrss.rest.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	List<UserDto> getUsers(int page, int limit);
	UserDto getUserByUserId(String userId);
	UserDto getUserByEmail(String email);
	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, String userId);
	void deleteUser(String userId, String email);

}
