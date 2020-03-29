package wrss.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import wrss.rest.dto.PostDto;
import wrss.rest.dto.UserDto;
import wrss.rest.entity.PostEntity;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.PostServiceException;
import wrss.rest.exception.UserServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.PostRepository;
import wrss.rest.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private Functions functions;
	
	@Override
	public List<PostDto> getAllPosts(int page, int limit) {
		
		if (page <= 0) throw new PostServiceException("Pages starts with 1");
		
		Pageable pageRequest = PageRequest.of(page-1, limit);
		
		Page<PostEntity> postsEntityPage = postRepository.findAll(pageRequest);
		List<PostEntity> postsEntity = postsEntityPage.getContent();
		
		if (postsEntity.isEmpty()) throw new PostServiceException("Empty");
		
		List<PostDto> posts = new ArrayList<>();
		for (PostEntity temp : postsEntity) {
			
			PostDto post = new ModelMapper().map(temp, PostDto.class);
			posts.add(post);
			
		}
		
		return posts;
	}
	
	@Override
	public List<PostDto> getPosts(String userId, int page, int limit) {
		
		if (page <= 0) throw new PostServiceException("Pages starts with 1");
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) throw new UserServiceException("User <" + userId + "> not found");
		
		Pageable pageRequest = PageRequest.of(page-1, limit);
		
		Page<PostEntity> postsEntityPage = postRepository.findAllByUser(userEntity, pageRequest);
		List<PostEntity> postsEntity = postsEntityPage.getContent();
		
		if (postsEntity.isEmpty()) throw new PostServiceException("Empty");
		
		List<PostDto> posts = new ArrayList<>();
		for (PostEntity temp : postsEntity) {
			
			PostDto post = new ModelMapper().map(temp, PostDto.class);
			posts.add(post);
		}
		
		return posts;
	}
	
	@Override
	public PostDto getPost(int postId) {
		
		PostEntity postEntity = postRepository.findById(postId);
		
		if (postEntity == null) throw new PostServiceException("Post with id <" + postId + "> not found");
		
		UserDto userDto = new ModelMapper().map(postEntity.getUser(), UserDto.class);
		
		PostDto post = new ModelMapper().map(postEntity, PostDto.class);
		post.setUserDto(userDto);
		
		return post;
	}
	
	@Override
	public PostDto createPost(PostDto postDto, String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(postDto.getAuthenticationEmail());
		
		String customMessage = "No access";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		PostEntity postEntity = new ModelMapper().map(postDto, PostEntity.class);
		postEntity.setUser(userEntity);
		
		PostEntity savedPostEntity = postRepository.save(postEntity);
		
		PostDto savedPost = new ModelMapper().map(savedPostEntity, PostDto.class);
		
		return savedPost;
	}

	@Override
	public PostDto updatePost(PostDto postDto, String userId, int postId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(postDto.getAuthenticationEmail());
		
		String customMessage = "No access";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		PostEntity post = postRepository.findById(postId);
		
		if (post == null) throw new PostServiceException("Post with id <" + postId + "> not found");
		
		post.setTitle(postDto.getTitle());
		post.setMessage(postDto.getMessage());
		
		PostEntity savedPostEntity = postRepository.save(post);
		
		PostDto savedPost = new ModelMapper().map(savedPostEntity, PostDto.class);
		
		return savedPost;
	}

	@Override
	public void deletePost(String userId, int postId, String email) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserEntity authenticationUserEntity = userRepository.findByEmail(email);
		
		String customMessage = "No access";
		functions.authenticationExceptions(userEntity, authenticationUserEntity, userId, customMessage);
		
		PostEntity post = postRepository.findById(postId);
		
		if (post == null) throw new PostServiceException("Post with id <" + postId + "> not found");
		
		postRepository.delete(post);
		
	}

}
