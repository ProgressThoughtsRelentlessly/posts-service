package com.pthore.service.posts.miniPostsStrategies;

import java.util.List;

import com.pthore.service.posts.documents.MiniPost;
import com.pthore.service.posts.miniPostsStrategies.MiniPostsClient.MiniPostClientInput;

/*
Reason behind STRATEGY pattern:
	
	. As every strategy will have different Aggregation implementation and use of UserInformation.
	 I think its better to have dedicated class for each strategy.
	 
	. Or Atleast might be required in the near future implementations to come up with 
	 more strategies. like Strategy containing only images, only texts, or based on the comments or 
	 upvotes or any other strategy thats required based on the Analytics that found after 
	 Kafa Streams implementation like based on userInteraction and activity.
	 
	. Also it contributes to selecting the best strategy For Search results based on some 
	 preprocessing such as popular authorname, popular domain name, popular titles/keywords
	 these insights are got by Analytics that the application would be generating based user Activity.
	
	. And helps fetch posts based on the community/Official Groups on Pthore!!!
*/

public interface MiniPostStrategy {
	
	public List<MiniPost> getMiniPosts( int page, MiniPostClientInput input) throws Exception ;
}
