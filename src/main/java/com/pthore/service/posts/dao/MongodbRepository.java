package com.pthore.service.posts.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Size;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.documents.Post2;
import com.pthore.service.posts.dto.PostDto;
import com.pthore.service.posts.dto.PostDto2;

@Repository
public class MongodbRepository {

	private final Logger logger = LoggerFactory.getLogger(MongodbRepository.class);
	
	@Autowired
	MongoTemplate mongoTemplate;


	public PostDto getPostDtoByPostId(String postId) throws Exception {
		
		MatchOperation matchStep = Aggregation.match(Criteria.where("_id").is(new ObjectId(postId)));
		
		ProjectionOperation projectStep = Aggregation
				.project("_id", "postTitle", "elementType", "postData", "imageCount")
				.and("_id").as("_id")
				.and("postTitle").as("postTitle")
				.and("postMetadata.elementTypes").as("elementTypes")
				.and("postData.postTextData").as("postData")
				.and(Size.lengthOfArray("postData.desktopImages")).as("imageCount");
		
		Aggregation aggregation  = Aggregation.newAggregation(matchStep, projectStep);
		PostDto postDto = mongoTemplate.aggregate(aggregation, "posts", PostDto.class).getUniqueMappedResult();
		return postDto;
	}

	public List<MiniPost> getMiniPosts(Aggregation newAggregation) {
	
		AggregationResults<MiniPost> results = mongoTemplate.aggregate(newAggregation,  "posts", MiniPost.class);
		List<MiniPost> miniPosts = results.getMappedResults();
		logger.info("fetched {} minipost", miniPosts.size());
		return miniPosts;
	}

	/*
	 	query:
	 		db.posts.aggregate([
	 			{ $match: {_id: :id} },
	 			{$project: {image: {$slice: {"postData.desktopImages", :index, 1}}}}
	 		])
	*/
	public byte[] getImage(String postId, int index, boolean isDesktopVersion) {
		
		String imageType = isDesktopVersion ? "desktopImages": "mobileImages";
		
		MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(new ObjectId(postId)));
		ProjectionOperation projectionOperation  = Aggregation
				.project("image")
				.and("postData."+ imageType).slice(1, index).as("image");
		
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation);
		try {
			AggregationResults<byte[]> results = mongoTemplate.aggregate(aggregation, "posts", byte[].class);
			byte[] image = results.getMappedResults().get(0);
			return image;
		} catch (Exception ex) {
			logger.error("Error occurred while fetching the image. (guess the index was out of bound!)");
			return null;
		}
	}

	public Post2 savePost2(Post2 post) {
		post = this.mongoTemplate.save(post, "posts");
		return post;
	}

	public Post2 findPostById(String id) {
		
		Post2 post = this.mongoTemplate.findById(new ObjectId(id), Post2.class);
		return post;
	}

	public void removePostSectionWithNodeId(String postId, String nodeId) {

		Criteria criteria = Criteria
				.where("_id").is(new ObjectId(postId))
				.and("postNodes."+nodeId).exists(true);
		
		Query query = new Query(criteria);
		this.mongoTemplate.remove(query, "posts");
	}

	public Post2 changePostStatus(String postId, String status) {
		Criteria criteria = Criteria.where("_id").is(new ObjectId(postId));		
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("postStatus", status);
		this.mongoTemplate.updateFirst(query, update, "posts");
		return this.findPostById(postId);
	}
	
}
