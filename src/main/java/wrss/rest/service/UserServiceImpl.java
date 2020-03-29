package wrss.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private Functions functions; 

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		if (page <= 0) throw new UserServiceException("Pages starts with 1");
		
		Pageable pageRequest = PageRequest.of(page-1, limit);
		
		Page<UserEntity> usersEntityPage = userRepository.findAll(pageRequest);
		List<UserEntity> usersEntity = usersEntityPage.getContent();
		
		if (usersEntity.isEmpty()) throw new UserServiceException("Empty");
		
		List<UserDto> users = new ArrayList<>();
		for (UserEntity temp : usersEntity) {
			
			UserDto user = new UserDto();
			BeanUtils.copyProperties(temp, user);
			users.add(user);
			
		}
		
		return users;
	}	
	
	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) throw new UserServiceException("User <" + userId + "> not found");
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto user = modelMapper.map(userEntity, UserDto.class);

		return user;
	}	
	
	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) throw new UserServiceException("User <" + email + "> not found");
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		
		return userDto;
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		if ((userRepository.findByEmail(userDto.getEmail()) != null) ||
			(userRepository.findByUsername(userDto.getUsername()) != null)) {
			throw new UserServiceException("User allready exist");
		} 
		
		UserDetailsDto userDetailsDto = userDto.getUserDetails();
		userDetailsDto.setUserDto(userDto);
		userDto.setUserDetails(userDetailsDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setUserId(functions.generateUserId());
		userEntity.setEncryptedPassword(encoder.encode(userDto.getPassword()));
		
		UserEntity savedEntity = userRepository.save(userEntity);
		
		UserDto savedDto = new UserDto();
		BeanUtils.copyProperties(savedEntity, savedDto);
		
		return savedDto;
	}


	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(userDto.getAuthenticationEmail());
		
		String customMessage = "You can't update this user";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		userEntity.setEncryptedPassword(encoder.encode(userDto.getPassword()));

		if (!(userEntity.getUsername().equals(userDto.getUsername()))) {
			if (userRepository.findByUsername(userDto.getUsername()) != null) {
				throw new UserServiceException("Username <" + userDto.getUsername() + "> allready in database");
			}
		}
		userEntity.setUsername(userDto.getUsername());
		
		if (!(userEntity.getEmail().equals(userDto.getEmail()))) {
			if (userRepository.findByEmail(userDto.getEmail()) != null) {
				throw new UserServiceException("Email <" + userDto.getEmail() + "> allready in database");
			}
		}
		userEntity.setEmail(userDto.getEmail());
		
		UserEntity updatedEntity = userRepository.save(userEntity);
		
		UserDto updatedDto = new ModelMapper().map(updatedEntity, UserDto.class);
		
		return updatedDto;

	}
	
	@Override
	public void deleteUser(String userId, String email) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(email);
		
		String customMessage = "You can't delete this user";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		userRepository.delete(userEntity);
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity user = userRepository.findByEmail(email);
		
		if (user == null)
			throw new UserServiceException("User <" + email + "> not found");

		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}

}
