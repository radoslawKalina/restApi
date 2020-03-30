package wrss.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import wrss.rest.dto.UserDetailsDto;
import wrss.rest.dto.UserDto;
import wrss.rest.models.request.UserDetailsRequestModel;
import wrss.rest.models.request.UserRequestModel;
import wrss.rest.models.request.UserWithDetailsRequestModel;
import wrss.rest.models.response.UserDetailsResponseModel;
import wrss.rest.models.response.UserResponseModel;
import wrss.rest.models.response.UserWithDetailsResponseModel;
import wrss.rest.service.UserDetailsService;
import wrss.rest.service.UserService;

@RestController
@RequestMapping("/api")
public class UsersController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@ApiOperation("Get list of users")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/users")
	public List<UserResponseModel> getUsers(@RequestParam(value="page", defaultValue="1") int page,
											@RequestParam(value="limit", defaultValue="10") int limit) {
		
		List<UserDto> usersDto = userService.getUsers(page, limit);
		List<UserResponseModel> users = new ArrayList<>();
		
		for (UserDto temp : usersDto) {
			
			UserResponseModel user = new ModelMapper().map(temp, UserResponseModel.class);
			Link userLink = linkTo(UsersController.class).slash("users").slash(user.getUserId()).withSelfRel();
			user.add(userLink);
			users.add(user);
		}
		
		return users;
	}

	@ApiOperation(value = "Get user by userId", 
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/users/{userId}")
	public UserWithDetailsResponseModel getUser(@PathVariable String userId) {
		
		UserDto userDto = userService.getUserByUserId(userId);
		
		UserWithDetailsResponseModel user = new ModelMapper().map(userDto, UserWithDetailsResponseModel.class);

		Link postsLink = linkTo(methodOn(PostsController.class).getPosts(1, 10, userId)).withRel("posts");
		user.add(postsLink);
		
		return user;
	}
	
	@ApiOperation(value = "Get user details by userId",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/users/{userId}/details")
	public UserDetailsResponseModel getUserDetails(@PathVariable String userId) {
		
		UserDetailsDto userDetailsDto = userDetailsService.getUserDetails(userId);
		
		UserDetailsResponseModel userDetails = new UserDetailsResponseModel();
		BeanUtils.copyProperties(userDetailsDto, userDetails);
		
		Link postsLink = linkTo(methodOn(UsersController.class).getUser(userId)).withRel("user");
		userDetails.add(postsLink);
		
		return userDetails;
	}
	
	@ApiOperation("Create new user")
	@PostMapping(path="/users")
			//consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
			//produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
	public UserWithDetailsResponseModel addUser(@RequestBody UserWithDetailsRequestModel userModel) {
		
		UserDto userDto = new ModelMapper().map(userModel, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		
		UserWithDetailsResponseModel userResponseModel = new ModelMapper()
				.map(createdUser, UserWithDetailsResponseModel.class);
		
		Link link = linkTo(methodOn(UsersController.class).getUser(userResponseModel.getUserId())).withSelfRel();
		userResponseModel.add(link);
		
		return userResponseModel;
	}
	
	@ApiOperation(value = "Update user",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@PutMapping(path="/users/{userId}")
	public UserWithDetailsResponseModel updateUser(@RequestBody UserRequestModel userModel, @PathVariable String userId,
												   @ApiIgnore Authentication auth) {
		
		UserDto userDto = new ModelMapper().map(userModel, UserDto.class);
		userDto.setAuthenticationEmail(auth.getName());
		
		UserDto updatedUser = userService.updateUser(userDto, userId);
		
		UserWithDetailsResponseModel userResponseModel = new ModelMapper().map(updatedUser,
				UserWithDetailsResponseModel.class);
		
		Link link = linkTo(methodOn(PostsController.class).getPosts(1, 10, userId)).withRel("posts");
		userResponseModel.add(link);

		return userResponseModel;
		
	}
	
	@ApiOperation(value = "Delete user",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@PutMapping(path="/users/{userId}/details")
	public UserWithDetailsResponseModel updateUserDetails(@RequestBody UserDetailsRequestModel userDetailsModel, @PathVariable String userId,
														  @ApiIgnore Authentication auth) {
		
		UserDetailsDto userDetailsDto = new ModelMapper().map(userDetailsModel, UserDetailsDto.class);
		userDetailsDto.setAuthenticationEmail(auth.getName());
		
		UserDto updatedUserDetails = userDetailsService.updateUserDetails(userDetailsDto, userId);
		
		UserWithDetailsResponseModel userResponseModel = new ModelMapper().map(updatedUserDetails, 
				UserWithDetailsResponseModel.class);
		
		Link link = linkTo(methodOn(UsersController.class).getUser(userId)).withRel("user");
		userResponseModel.add(link);
		
		Link postsLink = linkTo(methodOn(PostsController.class).getPosts(1, 10, userId)).withRel("posts");
		userResponseModel.add(postsLink);

		return userResponseModel;
		
	}
	
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@DeleteMapping(path="/users/{userId}")
	public HashMap<String, String> deleteUser(@PathVariable String userId, @ApiIgnore Authentication auth) {
		
		userService.deleteUser(userId, auth.getName());
		
		HashMap<String, String> map = new HashMap<>();
		String responseMessage = "User with id: " + userId + " deleted.";
		map.put("message", responseMessage);
		
		return map;
	}
	
}
