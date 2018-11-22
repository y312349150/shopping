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
import com.my.utils.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;

@Controller
public class LoginController {
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	private static String LOGIN = "login";
	private static String INDEX = "redirect:/";
	//跳转页面
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String loginGet() {
		return LOGIN;
		
	}
	//接收前端POST请求
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String loginPost(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response) {
		//1.验证参数
		String username= userEntity.getUsername();
		String password = userEntity.getPassword();
		if(StringUtils.isEmpty(username)) {
			request.setAttribute("username", "用户名为空");
		}
		if(StringUtils.isEmpty(password)) {
			request.setAttribute("password", "密码为空");
		}
		//2.调用用户登录接口
		ResponseBase responseBase = memberServiceFegin.login(userEntity);
		if(!responseBase.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "账号或者密码错误");
			return LOGIN;
		}
		//3.得到用户token，存入Cookie中
		LinkedHashMap data = (LinkedHashMap) responseBase.getData();
		String memberToken = (String) data.get("memberToken");
		if(StringUtils.isEmpty(memberToken)) {
			request.setAttribute("error", "Cookie已经失效");
		}
		CookieUtil.addCookie(response, Constants.COOKIE_TOKEN_MEMBER, memberToken, Constants.LOGIN_TOKEN_MEMBER_TIME);
		return INDEX;
		
	}
	
	// 生成qq授权登录链接
		@RequestMapping("/locaQQLogin")
		public String locaQQLogin(HttpServletRequest reqest) throws QQConnectException {
			String authorizeURL = new Oauth().getAuthorizeURL(reqest);
			return "redirect:" + authorizeURL;

		}
		//关联QQ登录后
		@RequestMapping("/qqLoginCallback")
		public String qqLoginCallback(HttpServletRequest reqest, HttpServletResponse response,HttpSession httpSession) throws QQConnectException {

			// 1.获取授权码Code
			// 2.使用授权码Code获取accessToken
			AccessToken accessTokenOj = new Oauth().getAccessTokenByRequest(reqest);
			if (accessTokenOj == null) {
				reqest.setAttribute("error", "QQ授权失败");
				return "error";
			}
			String accessToken = accessTokenOj.getAccessToken();
			if (accessToken == null) {
				reqest.setAttribute("error", "accessToken为null");
				return "error";
			}
			// 3.使用accessToken获取openid
			OpenID openidOj = new OpenID(accessToken);
			String userOpenId = openidOj.getUserOpenID();
			// 4.调用会员服务接口 使用userOpenId 查找是否已经关联过账号
			ResponseBase openUserBase = memberServiceFegin.findByOpenIdUser(userOpenId);
			if(openUserBase.getReturnCode().equals(Constants.HTTP_RES_CODE_201)){
				// 5.如果没有关联账号，跳转到关联账号页面
				httpSession.setAttribute("qqOpenid", userOpenId);
				return "qqrelation";
			}
			//6.已经绑定账号  自动登录 将用户token信息存放在cookie中
			LinkedHashMap dataTokenMap = (LinkedHashMap) openUserBase.getData();
			String memberToken=(String) dataTokenMap.get("memberToken");
			CookieUtil.addCookie(response, Constants.COOKIE_TOKEN_MEMBER, memberToken, Constants.LOGIN_TOKEN_MEMBER_TIME);
			return INDEX;
		}

		// 还未关联QQ登录
		@RequestMapping(value = "/qqRelation", method = RequestMethod.POST)
		public String qqRelation(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
			// 1.获取openid
			String qqOpenid=(String) httpSession.getAttribute("qqOpenid");
			if(StringUtils.isEmpty(qqOpenid)){
				request.setAttribute("error", "没有获取到openid");
				return "error";
			}
			
			// 2.调用登录接口，获取token信息
			userEntity.setOpenid(qqOpenid);
			ResponseBase loginBase = memberServiceFegin.qqLogin(userEntity);
			if (!loginBase.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
				request.setAttribute("error", "账号或者密码错误!");
				return LOGIN;
			}

			LinkedHashMap loginData = (LinkedHashMap) loginBase.getData();
			String memberToken = (String) loginData.get("memberToken");
			if (StringUtils.isEmpty(memberToken)) {
				request.setAttribute("error", "会话已经失效!");
				return LOGIN;
			}
			// 3.将token信息存放在cookie里面
			CookieUtil.addCookie(response, Constants.COOKIE_TOKEN_MEMBER, memberToken, Constants.LOGIN_TOKEN_MEMBER_TIME);
			return INDEX;
		}
}
