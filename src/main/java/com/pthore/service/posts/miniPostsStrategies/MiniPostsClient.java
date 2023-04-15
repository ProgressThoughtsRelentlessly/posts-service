package com.pthore.service.posts.miniPostsStrategies;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pthore.service.posts.documents.MiniPost;


@Service
public class MiniPostsClient {
	
	@Autowired
	private PostIdsStrategy postIdsStrategy;
	@Autowired
	private  AuthorStrategy authorStrategy;
	@Autowired
	private  DomainStrategy domainStrategy;
	@Autowired
	private GroupStrategy groupStrategy;
	@Autowired
	private TitleOrKeyWordStrategy titleOrKeywordStrategy;

	private Logger logger = LoggerFactory.getLogger(MiniPostsClient.class);
	
	public MiniPostsClient() {
	}
	
	private final String getStrategyName(MiniPostClientInput input) {
		
		// TODO: use advanced algorithm to choose an appropriate strategy or combination of them based on analytics data.
		if(input.strategy != null) 
			return input.strategy.toUpperCase();
		else if(input.strategy == null && input.inputString != null) {
			return "ALL";
		}
		return "";
	}
	
	
	private final MiniPostStrategy chooseStrategy(final MiniPostClientInput clientInput) throws IllegalArgumentException {
		
		MiniPostStrategy strategy;
		
		if(clientInput.strategy != null) {
			
			String strategyName = this.getStrategyName(clientInput);
			
			switch(strategyName) {
			case "ALL":
			case "TITLE":
				strategy = titleOrKeywordStrategy;
				break;
			case "DOMAIN":
				strategy = domainStrategy;
				break;
			case "AUTHOR":
				strategy = authorStrategy;
				break;
			case "POST_IDS":

				strategy = postIdsStrategy;
				break;
			default:
					throw new IllegalArgumentException("you can have ALL, TITLE, DOMAIN OR AUTHOR strategies only.");
			}
		} else if(clientInput.strategy == null && clientInput.inputString != null){
			strategy = titleOrKeywordStrategy; // This is for Search.
		} else {
			throw new IllegalArgumentException("You need to set the appropriate Client Input First");
		}
		
		return strategy;
	}
	
	public final List<MiniPost> getMiniPosts(final MiniPostClientInput input, final int page) throws Exception {
		
		return this.chooseStrategy(input).getMiniPosts(page, input);
	}
	
	
	public static class MiniPostClientInput {
		
		private String inputString;
		private String strategy;
		private Object inputObject;
		
		public MiniPostClientInput(String strategy, String inputString, Object inputObject) {
			this.strategy = strategy;
			this.inputString = inputString;
			this.inputObject = inputObject;
		}

		public String getInputString() {
			return inputString;
		}

		public String getStrategy() {
			return strategy;
		}

		public Object getInputObject() {
			return inputObject;
		}
		
		
	
	}
}
