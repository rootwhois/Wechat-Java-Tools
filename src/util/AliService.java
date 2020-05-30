package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.aliyun.com.viapi.FileUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.facebody.model.v20191230.RecognizePublicFaceRequest;
import com.aliyuncs.facebody.model.v20191230.RecognizePublicFaceResponse;
import com.aliyuncs.ocr.model.v20191230.RecognizeCharacterRequest;
import com.aliyuncs.ocr.model.v20191230.RecognizeCharacterResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import api.util.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AliService {

	private final static String ACCESSKEYID = "";	//阿里云的AccessKeyID
	private final static String ACCESSSCRIET = "";	//阿里云的AccessSecret
	private final static String APPCODE = ""; // 你自己的AppCode

	/**
	 * 公众人物图片识别
	 * 
	 * @author 陈广生
	 * @param url
	 * @return
	 */
	public static String getPublicFace(String url) {
		String result = "查无此人";
		String json = "";

		DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", ACCESSKEYID, ACCESSSCRIET);
		IAcsClient client = new DefaultAcsClient(profile);

		RecognizePublicFaceRequest request = new RecognizePublicFaceRequest();

		List<RecognizePublicFaceRequest.Task> taskList = new ArrayList<RecognizePublicFaceRequest.Task>();

		RecognizePublicFaceRequest.Task task1 = new RecognizePublicFaceRequest.Task();
		task1.setImageURL(url);
		taskList.add(task1);
		request.setTasks(taskList);

		try {
			RecognizePublicFaceResponse response = client.getAcsResponse(request);
			json = new Gson().toJson(response);
			// System.out.println(json);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			System.out.println("ErrCode:" + e.getErrCode());
			System.out.println("ErrMsg:" + e.getErrMsg());
			System.out.println("RequestId:" + e.getRequestId());
		}

		JSONObject jsonObject = JSONObject.fromObject(json).getJSONObject("data");
		JSONArray jsonArray = jsonObject.getJSONArray("elements");
		jsonObject = jsonArray.getJSONObject(0);
		jsonArray = jsonObject.getJSONArray("results");
		jsonObject = jsonArray.getJSONObject(0);
		jsonArray = jsonObject.getJSONArray("subResults");
		if (jsonArray.isEmpty() == false) {
		jsonObject = jsonArray.getJSONObject(0);
		jsonArray = jsonObject.getJSONArray("faces");
			jsonObject = jsonArray.getJSONObject(0);
			String name = (String) jsonObject.get("name");
			double rate = (double) jsonObject.get("rate");

			result = "该图的人有" + rate + "%的概率是" + name;
		} 
		return result;
	}

	/**
	 * 智能回复
	 * 
	 * @author 陈广生
	 * @param question
	 * @return
	 */
	public static String inteligentChat(String question) {
		final String host = "https://jisuiqa.market.alicloudapi.com";
		final String path = "/iqa/query";
		String method = "post";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + APPCODE);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("question", question);
		String content = "哎呀，暂时出问题了。。。";

		try {
			HttpResponse result = HttpUtils.doGet(host, path, method, headers, querys);
			// 获取response的body
			InputStream is = result.getEntity().getContent();
			byte[] bytes = new byte[0];
			bytes = new byte[is.available()];
			is.read(bytes);
			String str = new String(bytes);

			JSONObject json = JSONObject.fromObject(str);
			if (json.getInt("status") != 0) {
				System.out.println(json.getString("msg"));
			} else {
				JSONObject resultarr = json.optJSONObject("result");
//                String type = resultarr.getString("type");
				content = resultarr.getString("content");
//                String relquestion = resultarr.getString("relquestion");
//                System.out.println(type + " " + content + " " + relquestion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * OCR识别转换
	 * 
	 * @author 陈广生
	 * @param url
	 * @return
	 */
	public static String switString(String url) {
		String result = "没有识别到图片中的文字";
		String json = "";

		DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", ACCESSKEYID, ACCESSSCRIET);
		IAcsClient client = new DefaultAcsClient(profile);

		try {
			RecognizeCharacterRequest request = new RecognizeCharacterRequest();
			String uploadFile = uploadFile(url);
			request.setImageURL(uploadFile);
			request.setMinHeight(10); // 图片中文字的最小高度，单位像素。
			request.setOutputProbability(true); // 是否输出文字框的概率。取值true或者false。
			RecognizeCharacterResponse response = client.getAcsResponse(request);
			json = new Gson().toJson(response);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			System.out.println("ErrCode:" + e.getErrCode());
			System.out.println("ErrMsg:" + e.getErrMsg());
			System.out.println("RequestId:" + e.getRequestId());
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = JSONObject.fromObject(json).getJSONObject("data");
		JSONArray jsonArray = jsonObject.getJSONArray("results");

		if (jsonArray.isEmpty() == false) {
			//清空result的结果
			result = "";
			//int length = -1;
			//double probability = 0.0;
			ArrayList<HashMap<String,String>> lists = new ArrayList<HashMap<String,String>>();
			

			for (int i = 0; i < jsonArray.size(); i++) {
				HashMap<String,String> map = new HashMap<String,String>();
				jsonObject = jsonArray.getJSONObject(i);
				JSONObject fromObject = JSONObject.fromObject(jsonObject);
				map.put("probability", fromObject.getString("probability"));
				map.put("text", fromObject.getString("text"));
				lists.add(map);
			}
			
			//提取可能性最大的文字返回
//			for(HashMap<String, String> list : lists) {
//				if(probability < Double.valueOf(list.get("probability"))) {
//					probability = Double.valueOf(list.get("probability"));
//					result = list.get("text");
//				}
//			}
			
			//提取所有识别到的文本返回
			for (HashMap<String, String> list : lists) {
					result += list.get("text");
			}
			
			//将识别到的最大文字段返回
//			for (HashMap<String, String> list : lists) {
//				if(list.get("text").length() > length) {
//					length = list.get("text").length();
//					result = list.get("text");
//				}
//			}
			
		}
		return result; 
	}

	/**
	 * 将图片上传到阿里云华东上海OSS上
	 * 
	 * @author 陈广生
	 * @param url
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	private static String uploadFile(String url) throws ClientException, IOException {
		FileUtils fileUtils = FileUtils.getInstance(ACCESSKEYID, ACCESSSCRIET);
		String fileUrl = fileUtils.upload(url);
		System.out.println(fileUrl);
		return fileUrl;
	}

}
