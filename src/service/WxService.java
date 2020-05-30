package service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import model.BaseMsg;
import model.TextMsg;
import util.AliService;



public class WxService {
	private static final String TOKEN = "smtdns";	//修改你想要设置的token
	/**
	 * 接入验证签名
	 * @author 陈广生
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp, String nonce, String echostr, String signature) {
		/**
		 * 1）将token、timestamp、nonce三个参数进行字典序排序 
		 * 2）将三个参数字符串拼接成一个字符串进行sha1加密 
		 * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		 */
		
		String[] strs = new String[] {TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		String str = strs[0] + strs[1] + strs[2];
		String mysign = shaEncode(str);
		return mysign.equals(signature);
	}

	//sha1加密
	 public static String shaEncode(String inStr)  {
	        MessageDigest sha = null;
	        try {
	            sha = MessageDigest.getInstance("SHA");
	        } catch (Exception e) {
	            System.out.println(e.toString());
	            e.printStackTrace();
	            return "";
	        }
	 
	        byte[] byteArray = null;
			try {
				byteArray = inStr.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        byte[] md5Bytes = sha.digest(byteArray);
	        StringBuffer hexValue = new StringBuffer();
	        for (int i = 0; i < md5Bytes.length; i++) {
	            int val = ((int) md5Bytes[i]) & 0xff;
	            if (val < 16) {
	                hexValue.append("0");
	            }
	            hexValue.append(Integer.toHexString(val));
	        }
	        return hexValue.toString();
	    }

	 /**
	  * 解析xml数据包
	  * @author 陈广生
	  * @param inputStream
	  * @return
	  */
	public static Map<String, String> praseRequest(ServletInputStream inputStream) {
		HashMap<String, String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		try {
			//读取输入流，获取文档对象
			Document read = reader.read(inputStream);
			//根据文档对象，获取根节点
			Element rootElement = read.getRootElement();
			//获取根节点的所有的子节点
			List<Element> elements = rootElement.elements();
			for(Element e :elements) {
				map.put(e.getName(),e.getStringValue());
			}
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 参数	描述
		ToUserName	开发者微信号
		FromUserName	发送方帐号（一个OpenID）
		CreateTime	消息创建时间 （整型）
		MsgType	消息类型，文本为text
		Content	文本消息内容
		MsgId	消息id，64位整型
	 */
	/**
	 * 处理消息和事件
	 * @author 陈广生
	 * @param reqData
	 * @return
	 */
	public static String getResponse(Map<String, String> reqData) {
		BaseMsg msg = null;
		String MsgType = reqData.get("MsgType");
		switch(MsgType) {
			case "text":
				msg = dealTextMsg(reqData);
				break;
			case "voice":
				msg = dealTextMsg(reqData);
				break;
			case "image":
				msg = dealImageMsg(reqData);
				break;
			
			//其他事件
			default:
				
				break;
		}
		//xml封包
		return toXml(msg);
	}

	

	/**
	 * xml封包
	 * @author 陈广生
	 * @param msg
	 * @return
	 */
	private static String toXml(BaseMsg msg) {
		XStream stream = new XStream();
		stream.processAnnotations(BaseMsg.class);
		stream.processAnnotations(TextMsg.class);
		String xml = stream.toXML(msg);
		return xml;
	}

	//处理文本消息
	private static BaseMsg dealTextMsg(Map<String, String> reqData) {
		String reply = AliService.inteligentChat(reqData.get("Content"));
		TextMsg textMsg = new TextMsg(reqData, reply);
		return textMsg;
	}	
	private static BaseMsg dealImageMsg(Map<String, String> reqData) {
		String reply = AliService.switString(reqData.get("PicUrl"));
		//String reply = AliService.getPublicFace(reqData.get("PicUrl"));
		TextMsg textMsg = new TextMsg(reqData, reply);
		return textMsg;
	}
}
