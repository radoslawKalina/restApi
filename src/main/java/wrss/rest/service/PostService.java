package wrss.rest.service;

import java.util.List;

import wrss.rest.dto.PostDto;

public interface PostService {

	List<PostDto> getAllPosts(int page, int limit);
	List<PostDto> getPosts(String userId, int page, int limit);
	PostDto getPost(int postId);
	PostDto createPost(PostDto postDto, String userId);
	PostDto updatePost(PostDto postDto, String userId, int postId);
	void deletePost(String userId, int postId, String email);
}
