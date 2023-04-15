package com.pthore.service.posts.miniPostsStrategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pthore.service.posts.dao.IUserPostMetadataRepository;
import com.pthore.service.posts.dao.MongodbRepository;
import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.entities.Domain;
import com.pthore.service.posts.entities.UserPostMetadata;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient.MiniPostClientInput;
import com.pthore.service.posts.utils.AppConstant;


@Service
//@RequestScope
public class TitleOrKeyWordStrategy implements MiniPostStrategy {
	
	@Autowired
	private MongodbRepository mongodbRepository;
	
	@Autowired
	private IUserPostMetadataRepository userPostMetadataRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	public TitleOrKeyWordStrategy() {
	}
	/*
		Query:
			
			db.post.find({ 
				$or: [
					{title: {$regex: :keyword}},
					{title: {$regex: :keyword}},
					{title: {$regex: :keyword}} 
					...
					] 
				}, {<..mention all required fields...>})
		or 
			
			db.post.aggregate([
			{$match: {
				$or: [
						{title: {$regex: :keyword}},
						{title: {$regex: :keyword}},
						{title: {$regex: :keyword}} 
						...
					] 
				} }
			{$project: {....all required fields...}}
			])
	*/
	private final Aggregation getAggregation(String searchSentence, int page) {

		StringTokenizer tokenizer = new StringTokenizer(searchSentence.strip(), " ");
		List<Criteria> criteriaList = new ArrayList<>();
		
		while(tokenizer.hasMoreTokens()) {
			String searchToken = tokenizer.nextToken();
			criteriaList.add(Criteria.where("title").regex(".*" + searchToken + ".*"));
		}
		
		Criteria finalCriteria = Criteria.where("").orOperator(criteriaList);
		
		MatchOperation matchOperation = Aggregation.match(finalCriteria);
		SkipOperation skipOperation = Aggregation.skip(AppConstant.DEFAULT_PAGE_SIZE * page);
		ProjectionOperation projectionOperation = Aggregation
				.project("id", "postTitle", "miniPostData")
				.and("_id").as("postId")
				.and("postTitle").as("postTitle")
				.and("postData.postTextData").as("miniPostData");
		LimitOperation limitOperation  = Aggregation.limit(AppConstant.DEFAULT_PAGE_SIZE);
		return Aggregation.newAggregation(matchOperation, skipOperation, limitOperation, projectionOperation);
	}
	
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
					}					miniPost.setPostCreationDate(userPostMetadata.getPostCreationDate());
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
	
	public String preProcessSearchString(String sentence) {
		// remove common words.
		sentence  = sentence.toLowerCase()
				.replaceAll("\\bis\\b|\\bwas\\b|\\bthe\\b|\\bor\\b|\\band\\b|\\bto\\b|\\bi\\b|\\byou\\b|\\bhere\\b|\\bthere\\b", "");
		return sentence;
	}
	
	@Override
	public List<MiniPost> getMiniPosts(int page, MiniPostClientInput input) {
		
		String searchSentence = this.preProcessSearchString(input.getInputString());
		List<MiniPost> miniPosts = mongodbRepository.getMiniPosts(this.getAggregation(searchSentence, page));
		List<String> postIds = miniPosts.stream().map(post -> post.getPostId()).collect(Collectors.toList());
		List<UserPostMetadata> userPostMetadataList = userPostMetadataRepository.findByPostIdIn(postIds);
		this.setUserDetailsInMiniPost(miniPosts, userPostMetadataList);
				
		return miniPosts;
	}

}
