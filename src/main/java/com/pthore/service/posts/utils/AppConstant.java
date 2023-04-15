package com.pthore.service.posts.utils;

import java.awt.Dimension;

public interface AppConstant {
	// configure it to get it from the property file or config server.
	public final String IMAGE_RESOURCE_BASE_URL = "http://localhost:8080/api/posts/download/";
	public final Dimension DESKTOP_IMAGE_DIMENSION = new Dimension(800, 600);
	public final Dimension MOBILE_IMAGE_DIMENSION = new Dimension(400, 600);
	
	public final Long DEFAULT_PAGE_SIZE = 10L;
	
	public interface POST {
		
		public interface EVENT{
			
			public final String ADD_IMAGE = "add-image";
			public final String ADD_PARAGRAPH = "add-paragraph";
			
			public final String MOVE_UP = "move-up" ;
			public final String MOVE_DOWN = "move-down" ;
			
			public final String UPDATE_PARAGRAPH = "update-pragraph";

			public final String SAVE_AS_DRAFT = "save-as-draft";
			public final String REMOVE_SECTION = "remove-section";
			
			public final String CREATE = "create-post";
			public final String REMOVE_POST = "remove-post";
		}
		 
	}

}
