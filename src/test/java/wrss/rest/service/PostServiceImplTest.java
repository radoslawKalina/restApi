package wrss.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import wrss.rest.SharedMethods;
import wrss.rest.dto.PostDto;
import wrss.rest.entity.PostEntity;
import wrss.rest.entity.UserEntity;
import wrss.rest.exception.PostServiceException;
import wrss.rest.functions.Functions;
import wrss.rest.repository.PostRepository;
import wrss.rest.repository.UserRepository;

class PostServiceImplTest {
	
	@InjectMocks
	PostServiceImpl postService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	PostRepository postRepository;
	
	@Mock
	BCryptPasswordEncoder encoder;
	
	@Mock
	Functions functions; 
	
	private SharedMethods sharedMethods;
	
	private UserEntity userEntity;
	private PostEntity postEntity;

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sharedMethods = new SharedMethods();
		
		userEntity = sharedMethods.createUserEntity();
		
		postEntity = sharedMethods.createPostEntity();
		postEntity.setUser(userEntity);
		
	}

	@Test
	void testGetAllPosts_wrongPageNumber() {
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.getAllPosts(0, 10);
				});
	}

	@Test
	void testGetPosts_wrongPageNumber() {
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.getPosts("userId", 0, 10);
				});
	}
	
	@Test
	void testGetPosts_userWithThisUserIdNotFound() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.getPosts("userId", 0, 10);
				});
	}
	
	@Test
	void testGetPost() {
		
		when(postRepository.findById(anyInt())).thenReturn(postEntity);
		
		PostDto postDto = postService.getPost(1);
		
		assertNotNull(postDto);
		verify(postRepository, times(1)).findById(anyInt());
		assertEquals("title", postDto.getTitle());
		assertEquals("test@gmail.com", postDto.getUserDto().getEmail());
	}
	
	@Test
	void testGetPost_postWithThisIdNotFound() {
		
		when(postRepository.findById(anyInt())).thenReturn(null);
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.getPost(1);
				});
	}
	
	@Test
	void testCreatePost() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
		
		PostDto postDto = postService.createPost(sharedMethods.createPostDtoWithoutId(), "userId");
		
		assertNotNull(postDto);
		verify(postRepository, times(1)).save(any(PostEntity.class));
		assertEquals("title", postDto.getTitle());
		assertEquals("test@gmail.com", postDto.getUserDto().getEmail());
	}
	
	@Test
	void testUpdatePost() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		when(postRepository.findById(anyInt())).thenReturn(postEntity);
		when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
		
		PostDto postDto = postService.updatePost(sharedMethods.createPostDtoWithoutId(), "userId", 1);
		
		assertNotNull(postDto);
		verify(postRepository, times(1)).save(any(PostEntity.class));
		assertEquals("title", postDto.getTitle());
		assertEquals("test@gmail.com", postDto.getUserDto().getEmail());
	}
	
	@Test
	void testUpdatePost_postWithThisIdNotFound() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		when(postRepository.findById(anyInt())).thenReturn(null);
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.updatePost(sharedMethods.createPostDtoWithoutId(), "userId", 1);
				});
	}
	
	
	@Test
	void testDeletePost_postWithThisIdNotFound() {
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		when(postRepository.findById(anyInt())).thenReturn(null);
		
		assertThrows(PostServiceException.class, 
				
				()-> { postService.deletePost("userId", 1, "test@gmail.com");
				});
	}
	
}
