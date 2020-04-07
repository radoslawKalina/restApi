package wrss.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import wrss.rest.RestApiApplication;
import wrss.rest.SharedMethods;
import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.service.UserDetailsService;
import wrss.rest.service.UserService;


@SpringBootTest(webEnvironment=WebEnvironment.MOCK, classes={RestApiApplication.class})
class UsersControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserDetailsService userDetailsService;
	
	private MockMvc mockMvc;
	
	private SharedMethods sharedMethods;
	
	private UserDto userDto;
	
	@BeforeEach
	void setUp() throws Exception {
		
	    this.mockMvc = webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		sharedMethods = new SharedMethods();
		
		userDto = sharedMethods.createUserDto();
		userDto.setUserDetails(sharedMethods.createUserDetailsDto());
	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetUsers() throws Exception {
		
		List<UserDto> users = new ArrayList<>();
		users.add(userDto);
		users.add(userDto);
		users.add(userDto);
		
		when(userService.getUsers(anyInt(), anyInt())).thenReturn(users);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/users")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].links.length()").value(1))
				.andExpect(jsonPath("$[1].links.length()").value(1))
				.andExpect(jsonPath("$[2].links.length()").value(1))
				.andReturn();
	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetUser() throws Exception {
		
		when(userService.getUserByUserId(anyString())).thenReturn(userDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/users/DF54GED78B")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("userId"))
				.andExpect(jsonPath("$.username").value("username"))
				.andExpect(jsonPath("$.firstName").value("firstName"))
				.andExpect(jsonPath("$.lastName").value("lastName"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"))
				.andExpect(jsonPath("$.details.city").value("city"))
				.andExpect(jsonPath("$.details.street").value("street"))
				.andExpect(jsonPath("$.details.phoneNumber").value("999 999 999"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andExpect(jsonPath("$.id").doesNotExist())
				.andExpect(jsonPath("$.password").doesNotExist())
				.andExpect(jsonPath("$.encryptedPassword").doesNotExist())
				.andReturn();
	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetUserDetails() throws Exception {
		
		UserDetailsDto userDetailsDto = sharedMethods.createUserDetailsDtoWithId();
		userDetailsDto.setUserDto(userDto);
		
		when(userDetailsService.getUserDetails(anyString())).thenReturn(userDetailsDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/users/DF54GED78B/details")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value("city"))
				.andExpect(jsonPath("$.street").value("street"))
				.andExpect(jsonPath("$.phoneNumber").value("999 999 999"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andExpect(jsonPath("$.id").doesNotExist())
				.andExpect(jsonPath("$.userDto").doesNotExist())
				.andReturn();
		
	}

	@Test
	void testAddUser() throws Exception {
		
		UserDto requestUserDto = sharedMethods.createUserDtoRequest();
		requestUserDto.setUserDetails(sharedMethods.createUserDetailsDto());
		
		when(userService.createUser(any(UserDto.class))).thenReturn(userDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SharedMethods.asJsonString(requestUserDto))
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("userId"))
				.andExpect(jsonPath("$.username").value("username"))
				.andExpect(jsonPath("$.firstName").value("firstName"))
				.andExpect(jsonPath("$.lastName").value("lastName"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"))
				.andExpect(jsonPath("$.details.city").value("city"))
				.andExpect(jsonPath("$.details.street").value("street"))
				.andExpect(jsonPath("$.details.phoneNumber").value("999 999 999"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andExpect(jsonPath("$.id").doesNotExist())
				.andExpect(jsonPath("$.password").doesNotExist())
				.andExpect(jsonPath("$.encryptedPassword").doesNotExist())
				.andReturn();
	}
	
	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testUpdateUser() throws Exception {
		
		UserDto requestUserDto = sharedMethods.createUserDtoRequest();
		
		when(userService.updateUser(any(UserDto.class), anyString())).thenReturn(userDto);
				
		RequestBuilder request = MockMvcRequestBuilders
				.put("/api/users/DF54GED78B")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SharedMethods.asJsonString(requestUserDto))
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("userId"))
				.andExpect(jsonPath("$.username").value("username"))
				.andExpect(jsonPath("$.firstName").value("firstName"))
				.andExpect(jsonPath("$.lastName").value("lastName"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"))
				.andExpect(jsonPath("$.details.city").value("city"))
				.andExpect(jsonPath("$.details.street").value("street"))
				.andExpect(jsonPath("$.details.phoneNumber").value("999 999 999"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andExpect(jsonPath("$.id").doesNotExist())
				.andExpect(jsonPath("$.password").doesNotExist())
				.andExpect(jsonPath("$.encryptedPassword").doesNotExist())
				.andReturn();
	}
	
	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testDeleteUser() throws Exception {
		
		doNothing().when(userService).deleteUser(anyString(), anyString());
				
		RequestBuilder request = MockMvcRequestBuilders
				.delete("/api/users/DF54GED78B")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
	}
	
}
