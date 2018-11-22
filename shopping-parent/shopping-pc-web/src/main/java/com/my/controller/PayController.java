package com.my.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.fegin.PayServiceFegin;

@Controller
public class PayController {
	@Autowired
	private PayServiceFegin payServiceFegin;
	@RequestMapping("/alipay")
	public void alipay(@RequestParam("payToken") String payToken,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		//1.验证参数
		if(StringUtils.isEmpty(payToken)) {
			return;
		}
		//2.调用支付服务接口，获取html元素
		ResponseBase findPayToken = payServiceFegin.findPayToken(payToken);
		if(!findPayToken.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
			String msg = findPayToken.getMsg();
			writer.write(msg);
			return;
		}
		//3.返回html给客户端
		LinkedHashMap data = (LinkedHashMap) findPayToken.getData();
		String html = (String) data.get("payHtml");
		//4.在页面上进行渲染
		writer.println(html);
		writer.close();
	}
}
