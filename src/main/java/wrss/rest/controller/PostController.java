package wrss.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import wrss.rest.dto.PostDto;
import wrss.rest.models.request.PostRequestModel;
import wrss.rest.models.response.PostResponseModel;
import wrss.rest.service.PostService;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@GetMapping(path="/posts", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<PostResponseModel> getAllPosts(@RequestParam(value="page", defaultValue="1") int page,
											   @RequestParam(value="limit", defaultValue="10") int limit) {
		
		List<PostDto> postsDto = postService.getAllPosts(page, limit);
		
		List<PostResponseModel> posts = new ArrayList<>();
		for (PostDto temp: postsDto) {
			
			PostResponseModel postResponseModel = new ModelMapper().map(temp, PostResponseModel.class);
			posts.add(postResponseModel);
		}
		
		return posts;
	}
	
	@GetMapping(path="/posts/{postId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public PostResponseModel getPost(@PathVariable int postId) {
		
		PostDto postDto = postService.getPost(postId);
		
		PostResponseModel post = new ModelMapper().map(postDto, PostResponseModel.class);

		return post;
	}
	
	@GetMapping(path="/users/{userId}/posts", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<PostResponseModel> getPosts(@RequestParam(value="page", defaultValue="1") int page,
											@RequestParam(value="limit", defaultValue="10") int limit,
											@PathVariable String userId) {
		
		List<PostDto> postsDto = postService.getPosts(userId, page, limit);
		
		List<PostResponseModel> posts = new ArrayList<>();
		for (PostDto temp: postsDto) {
			
			PostResponseModel postResponseModel = new ModelMapper().map(temp, PostResponseModel.class);
			posts.add(postResponseModel);
		}
		
		return posts;
	}
	
	@PostMapping(path="/users/{userId}/posts",
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public PostResponseModel addUser(@RequestBody PostRequestModel postModel, @PathVariable String userId,
									 Authentication auth) {
		
		PostDto postDto = new ModelMapper().map(postModel, PostDto.class);
		postDto.setAuthenticationEmail(auth.getName());
		
		PostDto createdPost = postService.createPost(postDto, userId);
		
		PostResponseModel postResponseModel = new ModelMapper().map(createdPost, PostResponseModel.class);
	
		return postResponseModel;
	}
	
	@PutMapping(path="/users/{userId}/posts/{postId}",
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public PostResponseModel updateUser(@RequestBody PostRequestModel postModel, @PathVariable String userId,
										@PathVariable int postId, Authentication auth) {
		
		PostDto postDto = new ModelMapper().map(postModel, PostDto.class);
		postDto.setAuthenticationEmail(auth.getName());
		
		PostDto updatedPost = postService.updatePost(postDto, userId, postId);
		
		PostResponseModel postResponseModel = new ModelMapper().map(updatedPost, PostResponseModel.class);
	
		return postResponseModel;
	}
	
	@DeleteMapping(path="/users/{userId}/posts/{postId}",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public HashMap<String, String> deleteUser(@PathVariable String userId, @PathVariable int postId,
											  Authentication auth) {
		
		postService.deletePost(userId, postId, auth.getName());
		
		HashMap<String, String> map = new HashMap<>();
		String responseMessage = "Post with id: " + postId + " deleted.";
		map.put("message", responseMessage);
		
		return map;
	}
}
