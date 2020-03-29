package wrss.rest.functions;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

import wrss.rest.entity.UserEntity;
import wrss.rest.exception.UserServiceException;

@Component
public class Functions {

	public final String ALPHABET = "ABCDEFGHIJKLMNOPRSTUWXYZ01234567890";
	public final Random RANDOM = new SecureRandom();
	
	public String generateUserId() {
		
		StringBuilder userId = new StringBuilder(10);
		
		for (int i=0; i < 10; i++) {
			userId.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(userId);
	}
	
	public void authenticationExceptions(UserEntity userEntity, UserEntity authenticationUserEntity,
										 String userId, String customMessage) {
		
		if (userEntity == null) throw new UserServiceException("User <" + userId + "> not found");
		
		if (authenticationUserEntity == null) throw new UserServiceException(
				"You did not relog after mail change. Please do it.");
		
		if (!(userEntity.getUserId().equals(authenticationUserEntity.getUserId()))) {
			throw new UserServiceException(customMessage);
		}
		
	}
	
}
