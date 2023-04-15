package com.pthore.service.posts.miniPostsStrategies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pthore.service.posts.dao.IUserPostMetadataRepository;
import com.pthore.service.posts.dao.MongodbRepository;
import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient.MiniPostClientInput;


@Service
//@RequestScope
public class GroupStrategy implements MiniPostStrategy {


	@Autowired
	private MongodbRepository mongodbRepository;
	
	@Autowired
	private IUserPostMetadataRepository userPostMetadataRepository;
	
	
	
	public GroupStrategy() {
	}



	@Override
	public List<MiniPost> getMiniPosts(int page, MiniPostClientInput input) {
		
		return null;
	}

}
