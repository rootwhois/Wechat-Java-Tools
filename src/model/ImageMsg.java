package model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ImageMsg extends BaseMsg{
	@XStreamAlias("PicUrl")
	private String picUrl;
	@XStreamAlias("MediaId")
	private String mediaId;
	@XStreamAlias("MsgId")
	private String msgId;
	@XStreamAlias("Content") 
	private String content;
	
	
	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getMediaId() {
		return mediaId;
	}


	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}


	public String getMsgId() {
		return msgId;
	}


	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public ImageMsg() {
		super();
	}


	public ImageMsg(Map<String, String> reqMsg) {
		super(reqMsg);
	}


	@Override
	public String toString() {
		return "ImageMsg [picUrl=" + picUrl + ", mediaId=" + mediaId + ", msgId=" + msgId + ", content=" + content
				+ "]";
	}


	public ImageMsg(Map<String,String> reqMsg, String content) {
		super(reqMsg);
		this.setMsgType("image");
		this.content = content;
	}
	
}
