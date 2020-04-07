package wrss.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
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
import wrss.rest.dto.PostDto;
import wrss.rest.models.request.PostRequestModel;
import wrss.rest.models.response.PostResponseModel;
import wrss.rest.service.PostService;

@RestController
@RequestMapping("/api")
public class PostsController {
	
	@Autowired
	private PostService postService;
	
	@ApiOperation("Get list of all posts")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/posts")
	public List<PostResponseModel> getAllPosts(@RequestParam(value="page", defaultValue="1") int page,
											   @RequestParam(value="limit", defaultValue="10") int limit) {
		
		List<PostDto> postsDto = postService.getAllPosts(page, limit);
		
		List<PostResponseModel> posts = new ArrayList<>();
		for (PostDto temp: postsDto) {
			
			PostResponseModel postResponseModel = new ModelMapper().map(temp, PostResponseModel.class);
			
			Link link = linkTo(methodOn(PostsController.class).getPost(temp.getId())).withSelfRel();
			postResponseModel.add(link);
			
			posts.add(postResponseModel);
		}
		
		return posts;
	}
	
	@ApiOperation("Get post by id")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/posts/{postId}")
	public PostResponseModel getPost(@PathVariable int postId) {
		
		PostDto postDto = postService.getPost(postId);
		
		PostResponseModel post = new ModelMapper().map(postDto, PostResponseModel.class);
		
		Link link = linkTo(methodOn(UsersController.class).getUser(postDto.getUserDto().getUserId())).withRel("user");
		post.add(link);

		return post;
	}
	
	@ApiOperation(value = "Get user posts by userId",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@GetMapping(path="/users/{userId}/posts")
	public List<PostResponseModel> getPosts(@RequestParam(value="page", defaultValue="1") int page,
											@RequestParam(value="limit", defaultValue="10") int limit,
											@PathVariable String userId) {
		
		List<PostDto> postsDto = postService.getPosts(userId, page, limit);
		
		List<PostResponseModel> posts = new ArrayList<>();
		for (PostDto temp: postsDto) {
			
			PostResponseModel postResponseModel = new ModelMapper().map(temp, PostResponseModel.class);

			Link link = linkTo(methodOn(PostsController.class).getPost(temp.getId())).withSelfRel();
			postResponseModel.add(link);
			
			posts.add(postResponseModel);
		}
		
		return posts;
	}
	
	@ApiOperation(value = "Add new post",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@PostMapping(path="/users/{userId}/posts")
	public PostResponseModel addUser(@RequestBody PostRequestModel postModel, @PathVariable String userId,
									 @ApiIgnore Authentication auth) {
		
		PostDto postDto = new ModelMapper().map(postModel, PostDto.class);
		postDto.setAuthenticationEmail(auth.getName());
		
		PostDto createdPost = postService.createPost(postDto, userId);
		
		PostResponseModel postResponseModel = new ModelMapper().map(createdPost, PostResponseModel.class);
		
		Link link = linkTo(methodOn(PostsController.class).getPost(createdPost.getId())).withSelfRel();
		postResponseModel.add(link);
	
		return postResponseModel;
	}
	
	@ApiOperation(value = "Update post",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@PutMapping(path="/users/{userId}/posts/{postId}")
	public PostResponseModel updateUser(@RequestBody PostRequestModel postModel, @PathVariable String userId,
										@PathVariable int postId, @ApiIgnore Authentication auth) {
		
		PostDto postDto = new ModelMapper().map(postModel, PostDto.class);
		postDto.setAuthenticationEmail(auth.getName());
		
		PostDto updatedPost = postService.updatePost(postDto, userId, postId);
		
		PostResponseModel postResponseModel = new ModelMapper().map(updatedPost, PostResponseModel.class);
		
		Link link = linkTo(methodOn(PostsController.class).getPost(updatedPost.getId())).withSelfRel();
		postResponseModel.add(link);
	
		return postResponseModel;
	}
	
	@ApiOperation(value = "Delete post",
			notes = "For userId you should use that given in /api/login response headers")
	@ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
	@DeleteMapping(path="/users/{userId}/posts/{postId}")
	public HashMap<String, String> deleteUser(@PathVariable String userId, @PathVariable int postId,
											  @ApiIgnore Authentication auth) {
		
		postService.deletePost(userId, postId, auth.getName());
		
		HashMap<String, String> map = new HashMap<>();
		String responseMessage = "Post with id: " + postId + " deleted.";
		map.put("message", responseMessage);
		
		return map;
	}
}
