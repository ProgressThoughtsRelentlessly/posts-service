package com.pthore.service.posts.documents;

import java.util.List;

public class PostNode {
	
	private String nodeId;
	private Long sectionIdx;
	private String type;
	private String textData;
	private List<byte[]> desktopImages;
	private List<byte[]> mobileImages;
	
	
	
	@Override
	public String toString() {
		return "PostNode [nodeId=" + nodeId + ", sectionIdx=" + sectionIdx + ", type=" + type + ", textData=" + textData
				+ ", desktopImages=" + desktopImages + ", mobileImages=" + mobileImages + "]";
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public Long getSectionIdx() {
		return sectionIdx;
	}
	public void setSectionIdx(Long sectionIdx) {
		this.sectionIdx = sectionIdx;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTextData() {
		return textData;
	}
	public void setTextData(String textData) {
		this.textData = textData;
	}
	public List<byte[]> getDesktopImages() {
		return desktopImages;
	}
	public void setDesktopImages(List<byte[]> desktopImages) {
		this.desktopImages = desktopImages;
	}
	public List<byte[]> getMobileImages() {
		return mobileImages;
	}
	public void setMobileImages(List<byte[]> mobileImages) {
		this.mobileImages = mobileImages;
	}
	
	
}
