package com.my.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.api.entity.UserEntity;
import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.fegin.MemberServiceFegin;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;

@Controller
public class RegisterController {
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	private static String REGISTER = "register";
	private static String LOGIN = "login";
	private static String INDEX = "index";
	private static String ERROR = "error";
	//跳转注册页面
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String RegisterGet() {
		return REGISTER;

	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return INDEX;

	}
	//实现注册服务
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String RegisterPost(UserEntity userEntity,HttpServletRequest request) {
		//1.判断参数
		String username= userEntity.getUsername();
		String password = userEntity.getPassword();
		if(StringUtils.isEmpty(username)) {
			request.setAttribute("username", "用户名为空");
		}
		if(StringUtils.isEmpty(password)) {
			request.setAttribute("password", "密码为空");
		}
		//2.调用会员注册接口
		ResponseBase rb = memberServiceFegin.regUser(userEntity);
		//3.注册请求失败
		if(!rb.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "注册失败");
			return REGISTER;
		}
		
		//4.注册成功
		return LOGIN;

	}

}
