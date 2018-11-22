package com.my.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.fegin.MemberServiceFegin;
import com.my.utils.CookieUtil;

@Controller
public class IndexController {
	private static String INDEX="index";
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String index(HttpServletRequest request) {
		//1.从Cookie中获取token
		String token = CookieUtil.getUid(request, Constants.COOKIE_TOKEN_MEMBER);
		//2.如果Token存在，调用接口查询信息
		if(!StringUtils.isEmpty(token)) {
			ResponseBase responseBase = memberServiceFegin.findByToken(token);
			if(responseBase.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
				LinkedHashMap userData = (LinkedHashMap) responseBase.getData();
				String username = (String) userData.get("username");
				request.setAttribute("username", username);
			}
		}
		return INDEX;
	}
}
