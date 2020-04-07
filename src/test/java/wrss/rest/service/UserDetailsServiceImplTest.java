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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import wrss.rest.SharedMethods;
import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.UserDetailsEntity;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.UserDetailsRepository;
import wrss.rest.repository.UserRepository;

class UserDetailsServiceImplTest {
	
	@InjectMocks
	UserDetailsServiceImpl userDetailsService;
	
	@Mock
	UserDetailsRepository userDetailsRepository;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Functions functions;
	
	private SharedMethods sharedMethods;
	
	private UserEntity userEntity;
	private UserDetailsEntity userDetailsEntity;

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sharedMethods = new SharedMethods();
		
		userEntity = sharedMethods.createUserEntity();
		userDetailsEntity = sharedMethods.createUserDetailsEntity();
	}

	@Test
	void testGetUserDetails() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userDetailsRepository.findByUser(any(UserEntity.class))).thenReturn(userDetailsEntity);
		
		UserDetailsDto userDetailsDto = userDetailsService.getUserDetails("userId");
		
		assertNotNull(userDetailsDto);
		verify(userDetailsRepository, times(1)).findByUser(any(UserEntity.class));
		assertEquals("city", userDetailsDto.getCity());
	}
	
	@Test
	void testGetUsersDetails_userDoNotExist() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, 
				
				()-> { userDetailsService.getUserDetails("userId");
				});
	}
	
	@Test
	void testUpdateUserDetails() {
		
		UserDetailsDto userDetailsDto = sharedMethods.createUserDetailsDtoWithId();
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		when(userDetailsRepository.findByUser(any(UserEntity.class))).thenReturn(userDetailsEntity);

		when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(userDetailsEntity);
		
		UserDto updatedDetails = userDetailsService.updateUserDetails(userDetailsDto, "userId");
		
		assertNotNull(updatedDetails);
		verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
		assertEquals("city", updatedDetails.getUserDetails().getCity());
		assertEquals("username", updatedDetails.getUsername());
		
		}
}
