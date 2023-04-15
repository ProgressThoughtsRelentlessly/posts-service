package com.pthore.service.posts.servicesImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pthore.service.posts.dao.IUserPostMetadataRepository;
import com.pthore.service.posts.dao.IUserProfileRepository;
import com.pthore.service.posts.dao.MongodbRepository;
import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.documents.Post2;
import com.pthore.service.posts.documents.PostNode;
import com.pthore.service.posts.dto.PostDto;
import com.pthore.service.posts.dto.PostDto2;
import com.pthore.service.posts.dto.PostUpdateDto;
import com.pthore.service.posts.entities.UserPostMetadata;
import com.pthore.service.posts.entities.UserProfile;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient.MiniPostClientInput;
import com.pthore.service.posts.utils.AppConstant;
import com.pthore.service.posts.utils.ImageUtils;

@Service
public class PostService {
	
	private final Logger logger = LoggerFactory.getLogger(PostService.class);
	
	@Autowired
	private ImageUtils imageUtils;
	
	@Autowired
	private MongodbRepository mongodbRepository;
	
	@Autowired
	private MiniPostsClient miniPostsClient;
	
	@Autowired
	private IUserPostMetadataRepository userPostMetadataRepository;
	
	@Autowired
	private IUserProfileRepository userProfileRepository;
	
	
	private final byte[][] resizeTheImage(InputStream imageInputStream) throws IOException {
		
		BufferedImage image = ImageIO.read(imageInputStream);
		
		imageUtils = new ImageUtils();
		
		List<BufferedImage> resizedImages = imageUtils.resizeToMobileAndDesktopSize(
				image, 
				AppConstant.DESKTOP_IMAGE_DIMENSION, 
				AppConstant.MOBILE_IMAGE_DIMENSION, 
				true);
		
		ByteArrayOutputStream outputStream;
		byte[][] resizedImagesBytes = new byte[2][];
		for(int i = 0; i < 2; i++) {
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImages.get(0), "png", outputStream);
			resizedImagesBytes[i] = outputStream.toByteArray();
		}		
		return resizedImagesBytes;
	}
	

	private Post2 getPostFromDto(PostDto2 postRequest) {
		Post2 post  = new Post2();
		post.set_id(postRequest.get_id());
		post.setPostComments(postRequest.getPostComments());
		post.setPostNodes(postRequest.getPostNodes());
		post.setPostTitle(postRequest.getPostTitle());
		post.setPostStatus(postRequest.getPostStatus());
		return post;
	}
	
	private PostDto2 getDtoFromPost(Post2 post) {
		PostDto2 postdto  = new PostDto2();
		postdto.set_id(post.get_id());
		postdto.setPostComments(post.getPostComments());
		postdto.setPostNodes(post.getPostNodes());
		postdto.setPostTitle(post.getPostTitle());
		postdto.setPostStatus(post.getPostStatus());
		return postdto;
	}
	
	private Post2 addParagraphIntoPost(PostDto2 postRequest) {
		boolean isNewPost = postRequest.get_id() == null || postRequest.get_id().length() == 0;
		Post2 post = getPostFromDto(postRequest);
		if(isNewPost) {
			post = this.mongodbRepository.savePost2(post);
			return post;
		} else {
			Post2 post2 = this.mongodbRepository.findPostById(postRequest.get_id());
			
			PostNode node = post.getPostNodes().get(post.getPostNodes().size() -1);
			post2.getPostNodes().add(node);
			post2 = this.mongodbRepository.savePost2(post2);
			return post2;
		}
	}
	
	private Post2 addImageIntoPost(PostDto2 postRequest, InputStream imageInputStream) throws IOException {
		
		boolean isNewPost = postRequest.get_id() == null || postRequest.get_id().length() == 0;
		Post2 post = getPostFromDto(postRequest);
		byte[][] resizedImages = this.resizeTheImage(imageInputStream);
		
		
		if(isNewPost) {
			post.getPostNodes().get(0).setDesktopImages(Arrays.asList(resizedImages[0]));
			post.getPostNodes().get(0).setMobileImages(Arrays.asList(resizedImages[1]));
			post = this.mongodbRepository.savePost2(post);
			return post;
		} else {
			Post2 post2 = this.mongodbRepository.findPostById(postRequest.get_id());
			PostNode node = postRequest.getPostNodes().get(postRequest.getPostNodes().size() -1);
			node.setDesktopImages(Arrays.asList(resizedImages[0]));
			node.setMobileImages(Arrays.asList(resizedImages[1]));
			
			post2.getPostNodes().add(node);
			post2 = this.mongodbRepository.savePost2(post2);
			return post2;
		}
	}
	
	private Post2 removePostSection(PostDto2 postRequest) {
		Post2 post = this.mongodbRepository.findPostById(postRequest.get_id());
		ListIterator<PostNode> iterator = post.getPostNodes().listIterator();
		while(iterator.hasNext()) {
			boolean isMatch = iterator.next().getNodeId().equals(postRequest.getNodeId());
			if(isMatch) {
				iterator.remove();
				break;
			}
		}
		post = this.mongodbRepository.savePost2(post);
		return post;
	}
	
	private Post2 createPost(PostDto2 postRequest) {
		
		Post2 post = this.mongodbRepository.changePostStatus(postRequest.get_id(), "created");
		UserPostMetadata userPostMetadata = new UserPostMetadata();
		userPostMetadata.setPostId(postRequest.get_id());
		userPostMetadata.setPostTitle(postRequest.getPostTitle());
		userPostMetadata.setUpvotes(0L);
		userPostMetadata.setAuthorEmail(postRequest.getAuthorEmail());
		UserProfile userProfile = userProfileRepository.findByEmail(postRequest.getAuthorEmail()); // get this from JWT token or from requestParam
		userPostMetadata.setUserProfile(userProfile);

		userPostMetadataRepository.save(userPostMetadata);
		return post;
	}
	
	
	private Post2 updateExistingPostStructure(PostDto2 postRequest) {
		
		List<PostNode> postNodesFromRequest = postRequest.getPostNodes();
		postNodesFromRequest.sort((oldVal, newVal) -> oldVal.getNodeId().compareToIgnoreCase(newVal.getNodeId()));
		
		Post2 post = this.mongodbRepository.findPostById(postRequest.get_id());
		List<PostNode> postNodesFromDb = post.getPostNodes();
		postNodesFromDb.sort((oldVal, newVal) -> oldVal.getNodeId().compareToIgnoreCase(newVal.getNodeId()));
		
		for(int idx = 0; idx < postNodesFromRequest.size(); idx++) {
			postNodesFromDb.get(idx).setSectionIdx(postNodesFromRequest.get(idx).getSectionIdx());
			if(postNodesFromDb.get(idx).getType().equals("para")) {
				postNodesFromDb.get(idx).setTextData(postNodesFromRequest.get(idx).getTextData());
			}
		}
		post = this.mongodbRepository.savePost2(post);
		return post;
	}

	public final PostDto2 doPostOperations(PostDto2 postRequest, MultipartFile file) throws Exception {
		Post2 post = new Post2();
		switch(postRequest.getOperation()) {
		case AppConstant.POST.EVENT.ADD_PARAGRAPH:
			post = this.addParagraphIntoPost(postRequest);
			break;
		case AppConstant.POST.EVENT.ADD_IMAGE:
			post = this.addImageIntoPost(postRequest, file.getInputStream());
			break;
		case AppConstant.POST.EVENT.MOVE_DOWN:
			
		case AppConstant.POST.EVENT.MOVE_UP:
			
		case AppConstant.POST.EVENT.UPDATE_PARAGRAPH:
			
			post = updateExistingPostStructure(postRequest);
			break;
			
		case AppConstant.POST.EVENT.REMOVE_SECTION:
			post = this.removePostSection(postRequest);
			break;
		
		case AppConstant.POST.EVENT.SAVE_AS_DRAFT:
			post = this.mongodbRepository.changePostStatus(postRequest.get_id(), "saved");
			break;
		case AppConstant.POST.EVENT.CREATE:
			post = this.createPost(postRequest);
			break;
		default:
			break;
		}
		
		return this.getDtoFromPost(post);
	}

	private final PostDto getPostDtoDataInAppropriateFormat(PostDto postDto) throws InvalidAttributesException {
		// parse element types and set the postData appropriately.
		int imagePointer = 0; int index = 0;
		List<String> elementTypes = postDto.getElementTypes();
		List<String> postDataRef = postDto.getPostData();
		
		while(postDto.getElementTypes().size() < index) {
			
			switch(elementTypes.get(index)) {
			case "paragraph":
				
				break;
			case "title": 
				break;
			case "link": 
				break;
			case "image":
				postDataRef.add(index, AppConstant.IMAGE_RESOURCE_BASE_URL + "/" + postDto.get_id() + "/" + imagePointer + 1);
				imagePointer++;
				break;
			default:
					throw new InvalidAttributesException("post Dto contains unrecognized element type");
			}
			index++;
			
		}
		
		return postDto;
	}
	
	public PostDto getPostById(String postId) throws Exception {
		PostDto postDto = this.mongodbRepository.getPostDtoByPostId(postId);
		this.getPostDtoDataInAppropriateFormat(postDto);
		return postDto;
	}
	
	
	public List<MiniPost> getListOfMiniPostsBasedOnStrategy(String strategy, String inputString, int page, int offset ) throws Exception {
		/*
		 *  strategies: ALL, DOMAIN, AUTHOR, TITLE_TOKEN_MATCH, POST_IDS
		*/
		MiniPostClientInput miniPostClientInput = new MiniPostClientInput(strategy, inputString, null);
		List<MiniPost> miniPosts = miniPostsClient.getMiniPosts(miniPostClientInput, page);
		
		return miniPosts;
	}


	public byte[] getPostImage(String postId, int index, boolean isDesktopVersion) {
		byte[] image = mongodbRepository.getImage(postId, index, isDesktopVersion);
		return image;
	}

	
//	@Lookup
//	public MiniPostsClient getMiniPostClient() {
//		return null;
//	}
	
	public List<MiniPost> getListOfMiniPostsForGivenPostIds(List<String> postIds) throws Exception {
		
		MiniPostClientInput miniPostClientInput = new MiniPostClientInput("POST_IDS", "", postIds);
		List<MiniPost> miniPosts = miniPostsClient.getMiniPosts(miniPostClientInput, 1);
		
		return miniPosts;
	}


	public void upvotePost(String postId) {
		
		UserPostMetadata userPostMetadata = userPostMetadataRepository.findByPostIdIn(Collections.singletonList(postId)).get(0);
		userPostMetadata.setUpvotes(userPostMetadata.getUpvotes() + 1);
		userPostMetadataRepository.save(userPostMetadata);
	}

}
