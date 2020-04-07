package wrss.rest.controller;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import wrss.rest.dto.PostDto;
import wrss.rest.service.PostService;

@SpringBootTest(webEnvironment=WebEnvironment.MOCK, classes={RestApiApplication.class})
class PostsControllerTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private PostService postService;
	
	private MockMvc mockMvc;
	
	private SharedMethods sharedMethods;
	
	private PostDto postDto;

	@BeforeEach
	void setUp() throws Exception {
		
	    this.mockMvc = webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		sharedMethods = new SharedMethods();
	
		postDto = sharedMethods.createPostDtoWithId();
		postDto.setUserDto(sharedMethods.createUserDto());
	}
	

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetAllPosts() throws Exception {
		
		List<PostDto> posts = new ArrayList<>();
		posts.add(postDto);
		posts.add(postDto);
		
		when(postService.getAllPosts(anyInt(), anyInt())).thenReturn(posts);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/posts")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].links.length()").value(1))
				.andExpect(jsonPath("$[1].links.length()").value(1))
				.andReturn();
	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetPost() throws Exception {
		
		when(postService.getPost(anyInt())).thenReturn(postDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/posts/1")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("title"))
				.andExpect(jsonPath("$.message").value("message"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andReturn();
		
	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testGetPosts() throws Exception {
		
		List<PostDto> posts = new ArrayList<>();
		posts.add(postDto);
		posts.add(postDto);
		
		when(postService.getPosts(anyString(), anyInt(), anyInt())).thenReturn(posts);
		
		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/users/DF54GED78B/posts")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].links.length()").value(1))
				.andExpect(jsonPath("$[1].links.length()").value(1))
				.andReturn();
	}
		
	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testAddPost() throws Exception {

		PostDto postDtoRequest = sharedMethods.createPostDtoWithoutId();
		
		when(postService.createPost(any(PostDto.class), anyString())).thenReturn(postDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/api/users/DF54GED78B/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SharedMethods.asJsonString(postDtoRequest))
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.title").value("title"))
				.andExpect(jsonPath("$.message").value("message"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andReturn();

	}

	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testUpdatePost() throws Exception {
		
		PostDto postDtoRequest = sharedMethods.createPostDtoWithId();
		
		when(postService.updatePost(any(PostDto.class), anyString(), anyInt())).thenReturn(postDto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.put("/api/users/DF54GED78B/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(SharedMethods.asJsonString(postDtoRequest))
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.title").value("title"))
				.andExpect(jsonPath("$.message").value("message"))
				.andExpect(jsonPath("$.links.length()").value(1))
				.andReturn();

	}
		
	@Test
	@WithMockUser(username = "test@gmail.com", password = "test")
	void testDeletePost() throws Exception {
		
		doNothing().when(postService).deletePost(anyString(), anyInt(), anyString());
		
		RequestBuilder request = MockMvcRequestBuilders
				.delete("/api/users/DF54GED78B/posts/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		
	}

}
