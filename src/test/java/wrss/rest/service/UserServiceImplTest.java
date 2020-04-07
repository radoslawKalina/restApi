package wrss.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import wrss.rest.SharedMethods;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.UserRepository;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	BCryptPasswordEncoder encoder;
	
	@Mock
	Functions functions; 
	
	private SharedMethods sharedMethods;
	
	private UserEntity userEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sharedMethods = new SharedMethods();
		userEntity = sharedMethods.createUserEntity();
		
	}
	
	@Test
	void testGetUsers_wrongPageNumber() {
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.getUsers(0, 10);
				});
	}

	@Test
	void testGetUserByUserId() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUserByUserId("userId");
		
		verify(userRepository, times(1)).findByUserId(anyString());
		assertNotNull(userDto);
		assertEquals("username", userDto.getUsername());
		
	}
	
	@Test
	void testGetUserByUserId_userNotFound() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.getUserByUserId("userId");
				});

	}
	
	@Test
	void testGetUserByEmail() {
		
		UserDto userDto = userService.getUserByEmail("test@gmail.com");
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		assertNotNull(userDto);
		verify(userRepository, times(1)).findByEmail(anyString());
		assertEquals("username", userDto.getUsername());
		
	}
	
	@Test
	void testGetUserByEmail_userNotFound() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.getUserByUserId("userId");
				});

	}
	
	@Test
	void testCreateUser() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		userDto.setUserDetails(sharedMethods.createUserDetailsDto());
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(userRepository.findByUsername(anyString())).thenReturn(null);
		when(functions.generateUserId()).thenReturn("userId");
		when(encoder.encode(anyString())).thenReturn("encryptedPassword");
		
		ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
		when(userRepository.save(captor.capture())).thenReturn(userEntity);

		UserDto createdDto = userService.createUser(userDto);

		assertNotNull(createdDto);
		verify(userRepository, times(1)).save(any(UserEntity.class));
		assertEquals("test@gmail.com", captor.getValue().getEmail());
		assertEquals("city", captor.getValue().getUserDetails().getCity());
		
		}
	
	@Test
	void testCreateUser_userWithThisEmailAllreadyExist() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		userDto.setUserDetails(sharedMethods.createUserDetailsDto());
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.createUser(userDto);
				});

	}
	
	@Test
	void testCreateUser_userWithThisUsernameAllreadyExist() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		userDto.setUserDetails(sharedMethods.createUserDetailsDto());
		
		when(userRepository.findByUsername(anyString())).thenReturn(userEntity);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.createUser(userDto);
				});

	}
	
	@Test
	void testUpdateUser() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity).thenReturn(null);
		when(encoder.encode(anyString())).thenReturn("encryptedPassword");
		when(userRepository.findByUsername(anyString())).thenReturn(null);

		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDto createdDto = userService.updateUser(userDto, "userId");

		assertNotNull(createdDto);
		verify(userRepository, times(1)).save(any(UserEntity.class));
		
		}
	
	@Test
	void testUpdateUser_userWithThisUsernameAllreadyExist() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		userDto.setUsername("username2");
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity).thenReturn(null);
		when(encoder.encode(anyString())).thenReturn("encryptedPassword");
		when(userRepository.findByUsername(anyString())).thenReturn(userEntity);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.updateUser(userDto, "userId");
				});

	}
	
	@Test
	void testUpdateUser_userWithThisEmailAllreadyExist() {
		
		UserDto userDto = sharedMethods.createUserDtoRequest();
		userDto.setEmail("test2@gmail.com");
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity).thenReturn(userEntity);
		when(encoder.encode(anyString())).thenReturn("encryptedPassword");
		when(userRepository.findByUsername(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userService.updateUser(userDto, "userId");
				});

	}
	
	@Test
	void testLoadUserByUsername() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UserServiceException.class, 
				
				()-> { userService.loadUserByUsername("test@gmail.com");
				});

	}

}
