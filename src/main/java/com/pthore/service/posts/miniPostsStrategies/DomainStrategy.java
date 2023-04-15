package com.pthore.service.posts.miniPostsStrategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pthore.service.posts.dao.IDomainRepository;
import com.pthore.service.posts.dao.IUserPostMetadataRepository;
import com.pthore.service.posts.dao.MongodbRepository;
import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.entities.Domain;
import com.pthore.service.posts.entities.UserPostMetadata;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient.MiniPostClientInput;
import com.pthore.service.posts.utils.AppConstant;


@Service
//@RequestScope
public class DomainStrategy implements MiniPostStrategy {
	
	@Autowired
	private MongodbRepository mongodbRepository;
	
	@Autowired
	private IUserPostMetadataRepository userPostMetadataRepository;
	
	@Autowired
	private IDomainRepository domainRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	public DomainStrategy() {
	}

//	public DomainStrategy setDomain(String domain) {
//		this.domain = domain;
//		return this;
//	}
	
	private void setUserDetailsInMiniPost(List<MiniPost> miniPosts, List<UserPostMetadata> userPostMetadataList) {
		// Use Linear search to find appropriate elements as each Page has only 10 elements.
		
		for(int i = 0; i < userPostMetadataList.size(); i++) {
			UserPostMetadata userPostMetadata = userPostMetadataList.get(i);
			
			for(int j = 0; j < miniPosts.size(); j++) {
				MiniPost miniPost = miniPosts.get(j);
				
				if(miniPost.getPostId().equals(userPostMetadata.getPostId())) {
					
					miniPost.setAuthorName(userPostMetadata.getUserProfile().getEmail());
					String profilePicture = userPostMetadata.getUserProfile().getProfilePicture();
					boolean isBase64 = Base64.isBase64(profilePicture);
					if(isBase64) {
						miniPost.setThumbnail(Base64.decodeBase64(profilePicture));
					} else {
						// In case a image Link is set, then fetch the image data.
						byte[] image = restTemplate.getForEntity(profilePicture, byte[].class).getBody();
						miniPost.setThumbnail( image );
					}
					miniPost.setPostCreationDate(userPostMetadata.getPostCreationDate());
					List<Domain> domains = userPostMetadata.getTaggedDomains();
					
					Map<String, String> taggedDomainsWithLinks = new HashMap<>();
					
					for(int k = 0; k < domains.size(); k++) {
						Domain domain = domains.get(k);
						// TODO: NEED TO ADD APPROPRIATE DOMAIN LINKS
						taggedDomainsWithLinks.put(domain.getDomainName(), String.valueOf(domain.getId()));
					}
					miniPost.setTaggedDomainsWithLinks(taggedDomainsWithLinks);
				}
			}
		}
	}
	
	private final Aggregation getAggregation(List<ObjectId> postIds, int page) {
		Criteria criteria = Criteria.where("_id").in(postIds);
		MatchOperation matchOperation = Aggregation.match(criteria);
		SkipOperation skipOperation = Aggregation.skip(AppConstant.DEFAULT_PAGE_SIZE * page);
		LimitOperation limitOperation  = Aggregation.limit(AppConstant.DEFAULT_PAGE_SIZE);
		ProjectionOperation projectionOperation = Aggregation
				.project("id", "postTitle", "miniPostData", "imageCount")
				.and("_id").as("postId")
				.and("postTitle").as("postTitle")
				.and("postData.postTextData").as("miniPostData");
		
		return Aggregation.newAggregation(matchOperation, skipOperation,limitOperation, projectionOperation);
	}


	@Override
	public List<MiniPost> getMiniPosts(int page, MiniPostClientInput input) {
		
		String domainName = input.getInputString();
		Domain domain = domainRepository.findByDomainName(domainName);
		List<UserPostMetadata> userPostMetadataList = domain.getUserPostMetadataList();
		List<ObjectId> postIds = userPostMetadataList.stream().map(p -> new ObjectId(p.getPostId())).collect(Collectors.toList());
		List<MiniPost> miniPosts = mongodbRepository.getMiniPosts(this.getAggregation(postIds, page));
		this.setUserDetailsInMiniPost(miniPosts, userPostMetadataList);
		
		return miniPosts;
	}

}
