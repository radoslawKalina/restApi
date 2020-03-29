package wrss.rest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.UserDetailsEntity;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.UserDetailsRepository;
import wrss.rest.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private Functions functions;
	
	@Override
	public UserDetailsDto getUserDetails(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) throw new UserServiceException("User <" + userId + "> not found");
		
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUser(userEntity);
		
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		BeanUtils.copyProperties(userDetailsEntity, userDetailsDto);
		
		return userDetailsDto;
	}

	@Override
	public UserDto updateUserDetails(UserDetailsDto userDetailsDto, String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(userDetailsDto.getAuthenticationEmail());
		
		String customMessage = "You can't update this user";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUser(userEntity);
		
		userDetailsEntity.setCity(userDetailsDto.getCity());
		userDetailsEntity.setStreet(userDetailsDto.getStreet());
		userDetailsEntity.setPhoneNumber(userDetailsDto.getPhoneNumber());
		
		UserDetailsEntity updatedUserDetailsEntity = userDetailsRepository.save(userDetailsEntity);
		
		UserDetailsDto updatedUserDetails = new ModelMapper().map(updatedUserDetailsEntity, UserDetailsDto.class);
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		userDto.setUserDetails(updatedUserDetails);
		
		return userDto;
	}

}
