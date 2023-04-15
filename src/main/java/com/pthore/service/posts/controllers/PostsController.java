package com.pthore.service.posts.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.dto.MiniPostRequestDto;
import com.pthore.service.posts.dto.PostDto;
import com.pthore.service.posts.dto.PostDto2;
import com.pthore.service.posts.servicesImpl.PostService;

@RestController
@RequestMapping( value = "/api/posts")
@CrossOrigin(value= {"*"})
public class PostsController {
	
	private Logger log = LoggerFactory.getLogger(PostsController.class);
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@PostMapping(value ="/v2/create", name="create a new post")
	public ResponseEntity<?> createNewPost2(
			@RequestParam(value="files", required=false)MultipartFile file, 
			@ModelAttribute("postRequest") String postRequest) throws Exception {
		
		// log.info("received file of size = {} and postRequest = {}", file.getSize(), postRequest);
		
		PostDto2 postDto = mapper.reader().readValue(postRequest, PostDto2.class);
		
		log.info("post Request = {}, file = {}", postDto, file == null? null: file.getSize());
		postDto = null; // postService.doPostOperations(postRequest, file);
		return ResponseEntity.ok().body(postDto);
		
	}

	/* Api to upvote the Post.
	*/
	@GetMapping(value = "/upvote/{postId}")
	public ResponseEntity<?> upvotePost(@PathVariable String postId) {
		
		postService.upvotePost(postId);
		return ResponseEntity.ok().body("success");
	}
	
	/* Api to get the post based on post Id.
	*/
	@GetMapping(value = "/post/{id}")
	public ResponseEntity<PostDto> getPostByPostId(@PathVariable("id") String postId) throws Exception {
		PostDto postDto = postService.getPostById(postId);
		return ResponseEntity.ok().body(postDto);
	}
	
	/* Api to get mini-post-data. in Paginated manner.
	*/
	@PostMapping(value = "/miniPosts/{page}")
	public List<MiniPost> getMiniPostsBasedOnStrategy(@PathVariable("page") int page, 
			@RequestBody MiniPostRequestDto miniPostRequest)  throws Exception {
		
		String strategy = miniPostRequest.getStrategy();
		String inputString = miniPostRequest.getInputString();
		List<MiniPost> miniPosts = postService.getListOfMiniPostsBasedOnStrategy(strategy, inputString, page, 0);
		return miniPosts;
	}
	
	/*  Api to get Mini posts for given PostIds.
	*/
	@PostMapping(value = "/miniPosts")
	public List<MiniPost> getMiniPostsForGivenPostIds(@RequestBody List<String> postIds) throws Exception {
		
		// TODO: modify the api to work with MiniPostRequestDto as @RequestBody
		List<MiniPost> miniPosts = postService.getListOfMiniPostsForGivenPostIds(postIds);
		return miniPosts;
	}
	
	
	/*  Api to search the posts based on domain, author, title , or any keyword. [ service to create and handle posts metadata in mysql ]
	*/
	@GetMapping(value = "/search/{searchInput}/{page}")
	public List<MiniPost> getSearchResults(
			@PathVariable("page") int page, 
			@PathVariable("searchInput")String searchInput )  throws Exception {
		
		List<MiniPost> miniPosts = postService.getListOfMiniPostsBasedOnStrategy("ALL", searchInput, page, 0);
		return miniPosts;
	}
	
	/*  Api to get Resources like images/videos of posts | I'm thinking of creating a separate Resource server to handle this.
	*/
	@GetMapping(value = "/download/{postId}/{index}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] downloadPostImagesBasedOnPostIdAndImageIndex( 
			@PathVariable("postId") String postId, 
			@PathVariable("index") int index, @RequestHeader HttpHeaders headers)  throws Exception {
		boolean isDesktopVersion = headers.get("image_version").get(0).equals("desktop");
		byte[] image = postService.getPostImage(postId, index, isDesktopVersion);
		return image;
	}
	
	
	/*
	*/
	
	
}
