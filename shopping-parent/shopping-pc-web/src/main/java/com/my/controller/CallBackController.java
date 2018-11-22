package com.my.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.fegin.CallBackServiceFegin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/alibaba")
public class CallBackController {
	@Autowired
	private CallBackServiceFegin callBackServiceFegin;
	private String ERROR = "error";
	private String PAY_SUCCESS = "pay_success";
	//同步通知
	@RequestMapping("/return_url")
	public void synCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
			log.info("----------支付宝同步回调开始------");
			ResponseBase synCallBack = callBackServiceFegin.synCallBack(params);
			if(!synCallBack.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
				return ;
			}
			
			LinkedHashMap data = (LinkedHashMap) synCallBack.getData();
			//
			String outTradeNo = (String) data.get("outTradeNo");
			
			//支付宝交易号
			String tradeNo = (String) data.get("tradeNo");
	
			//付款金额
			String totalAmount = (String) data.get("totalAmount");

			//封装HTML,浏览器模拟POST提交
			String formHtml="<form name='punchout_form' method='post' action='http://127.0.0.1/alibaba/synSuccessPage' >\r\n" + 
					"	<input type='hidden' name='outTradeNo' value='+"+outTradeNo+"'>\r\n" + 
					"	<input type='hidden' name='tradeNo' value='"+tradeNo+"'>\r\n" + 
					"	<input type='hidden' name='totalAmount' value='"+totalAmount+"'>\r\n" + 
					"	<input type='submit' value='立即支付' style='display:none'>\r\n" + 
					"</form>\r\n" + 
					"<script>document.forms[0].submit();</script>";
			writer.println(formHtml);
			writer.close();
			log.info("----------支付宝同步回调结束------");
		}
		
	}
	
	//以POST请求方式隐藏get信息
	@RequestMapping(value="/synSuccessPage",method=RequestMethod.POST)
	public String synSuccessPage(HttpServletRequest request,String outTradeNo,String tradeNo,String totalAmount) {
		request.setAttribute("outTradeNo", outTradeNo);
		request.setAttribute("tradeNo", tradeNo);
		request.setAttribute("totalAmount", totalAmount);
		return PAY_SUCCESS;
		
	}
	//异步通知
	@RequestMapping("/notify_url")
	@ResponseBody
	public String asynCallBack(HttpServletRequest request,HttpServletResponse response) {
		log.info("----------支付宝异步回调开始------");
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			
			//乱码解决，这段代码在出现乱码时使用
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		String synCallBack = callBackServiceFegin.asynCallBack(params);
		log.info("----------支付宝异步回调结束------");
		return synCallBack;
	}
}
