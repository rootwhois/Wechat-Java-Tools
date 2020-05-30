package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.WxService;

@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WxServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		/**
		 * 参数	描述
			signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
			timestamp	时间戳
			nonce	随机数
			echostr	随机字符串
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		//校验请求
		PrintWriter writer = response.getWriter();
		if(WxService.check(timestamp,nonce,echostr,signature)) {
			//校验成功，原样返回echostr参数
			writer.print(echostr);
			//System.out.println("接入成功！");
		}else {
			//校验失败
			writer.print("校验失败");
			//System.out.println("接入失败！");
		}
		//清空缓存区
		writer.flush();
		writer.close();
	}

	//接受xml数据包
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		//解包
		Map<String ,String> reqData = WxService.praseRequest(request.getInputStream());
		String respXml = WxService.getResponse(reqData);
		
		PrintWriter writer = response.getWriter();
		writer.print(respXml);
		writer.flush();
		writer.close();
	}
}
