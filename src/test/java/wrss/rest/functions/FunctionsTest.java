package wrss.rest.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wrss.rest.SharedMethods;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;


class FunctionsTest {
	
	private Functions functions;
	
	private SharedMethods sharedMethods;
	
	private UserEntity userEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		
		functions = new Functions();
		sharedMethods = new SharedMethods();
		userEntity = sharedMethods.createUserEntity();
		
	}

	@Test
	void testGenerateUserId() {

		String userId = functions.generateUserId();
		
		assertEquals(10, userId.length());
	}
	
	@Test
	void testAuthenticationExceptions_userDoesNotExist() {
		
		assertThrows(UserServiceException.class, 
				
				()-> { functions.authenticationExceptions(null, userEntity, "userId", "Test message");;
				});
		

	}
	
	@Test
	void testAuthenticationExceptions_userDoNotRelogAfterEmailChange() {
		
		assertThrows(UserServiceException.class, 
				
				()-> { functions.authenticationExceptions(userEntity, null, "userId", "Test message");;
				});
		
	}
	
	@Test
	void testAuthenticationExceptions_authenticationFailed() {
		
		UserEntity authenticationUserEntity = sharedMethods.createUserEntity();
		authenticationUserEntity.setUserId("test");
		
		assertThrows(UserServiceException.class, 
				
				()-> { functions.authenticationExceptions(userEntity, authenticationUserEntity, "userId", "Test message");;
				});
		
	}

}
