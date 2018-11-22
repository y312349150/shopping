package com.my.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.api.entity.UserEntity;
import com.my.base.ResponseBase;

@RequestMapping("/member")
public interface MemberService {
	@RequestMapping("/findById")
	ResponseBase findById(long userId);
	//用户注册
	@RequestMapping("/regUser")
	ResponseBase regUser(@RequestBody UserEntity user);
	//用户登录
	@RequestMapping("/login")
	ResponseBase login(@RequestBody UserEntity user);
	
	@RequestMapping("/findByToken")
	ResponseBase findByToken(@RequestParam("token") String token);
	
	
	@RequestMapping("/findByOpenIdUser")
	ResponseBase findByOpenIdUser(@RequestParam("openId") String openId);
	
	@RequestMapping("/qqLogin")
	ResponseBase qqLogin(@RequestBody UserEntity user);
}
