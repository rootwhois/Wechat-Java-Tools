package model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMsg extends BaseMsg{
	
	@XStreamAlias("Content")
	private String content;

	@Override
	public String toString() {
		return "TextMsg [content=" + content + "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public TextMsg(Map<String,String> reqMsg, String content) {
		super(reqMsg);
		this.setMsgType("text");
		this.content = content;
	}

}
