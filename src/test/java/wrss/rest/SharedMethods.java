package wrss.rest;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import wrss.rest.dto.PostDto;
import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.PostEntity;
import wrss.rest.entity.UserDetailsEntity;
import wrss.rest.entity.UserEntity;

@Component
public class SharedMethods {
	
	public UserDto createUserDtoRequest() {
		
		UserDto userDto = new UserDto();
		
		userDto.setUsername("username");
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setEmail("test@gmail.com");
		userDto.setPassword("password");
		
		return userDto;
	}
	
	public void setInCreationProperties(UserDto userDto) {
		
		userDto.setId(1);
		userDto.setUserId("userId");
		userDto.setEncryptedPassword("encryptedPassword");
		
	}
	
	public UserDto createUserDto() {
		
		UserDto userDto = createUserDtoRequest();
		setInCreationProperties(userDto);
		
		return userDto;
	}
	
	public UserEntity createUserEntity() {
		
		UserDto userDto = createUserDto();
		
		return new ModelMapper().map(userDto, UserEntity.class);
	}
	
	public UserDetailsDto createUserDetailsDto() {
		
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		
		userDetailsDto.setCity("city");
		userDetailsDto.setStreet("street");
		userDetailsDto.setPhoneNumber("999 999 999");
		
		return userDetailsDto;
	}
	
	public UserDetailsDto createUserDetailsDtoWithId() {
		
		UserDetailsDto userDetailsDto = createUserDetailsDto();
		userDetailsDto.setId(1);

		
		return userDetailsDto;
	}
	
	public UserDetailsEntity createUserDetailsEntity() {
		
		return new ModelMapper().map(createUserDetailsDto(), UserDetailsEntity.class);
	}
	
	public PostDto createPostDtoWithoutId() {
		
		PostDto postDto = new PostDto();
		postDto.setTitle("title");
		postDto.setMessage("message");
		
		return postDto;
	}
	
	public PostDto createPostDtoWithId() {
		
		PostDto postDto = createPostDtoWithoutId();
		postDto.setId(1);
		
		return postDto;
	}
	
	public PostEntity createPostEntity() {
		
		return new ModelMapper().map(createPostDtoWithId(), PostEntity.class);
	}
	
	
	public static String asJsonString(Object dto) {
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
			
			return writer.writeValueAsString(dto);
		
		} catch (Exception exc) { 
			
			throw new RuntimeException(exc);
		}
	}

}
