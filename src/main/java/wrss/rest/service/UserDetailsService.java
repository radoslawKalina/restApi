package wrss.rest.service;

import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;

public interface UserDetailsService {

	UserDetailsDto getUserDetails(String userId);

	UserDto updateUserDetails(UserDetailsDto userDetailsDto, String userId);

}
