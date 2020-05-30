package model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class BaseMsg {
	@XStreamAlias("ToUserName")
	private String toUserName;
	@XStreamAlias("FromUserName")
	private String fromUserName;
	@XStreamAlias("CreateTime")
	private String createTime;
	@XStreamAlias("MsgType")
	private String msgType;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public BaseMsg() {
		super();
	}
	@Override
	public String toString() {
		return "BaseMsg [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime=" + createTime
				+ ", msgType=" + msgType + "]";
	}
	
	public BaseMsg(Map<String,String> reqMsg) {
		this.toUserName = reqMsg.get("FromUserName");
		this.fromUserName = reqMsg.get("ToUserName");
		this.createTime = System.currentTimeMillis() / 1000 + "";
	}
	
}
