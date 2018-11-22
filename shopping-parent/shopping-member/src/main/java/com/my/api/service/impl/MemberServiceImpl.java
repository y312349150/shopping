package com.my.api.service.impl;

import java.util.Date;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.my.api.entity.UserEntity;
import com.my.api.service.MemberService;
import com.my.base.BaseApiService;
import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.dao.MemberDao;
import com.my.mq.RegisterMailboxProducer;
import com.my.utils.MD5Util;
import com.my.utils.Tokenutils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	RegisterMailboxProducer registerMailboxProducer;
	@Value("${messages.queue}")
	private String MESSAGESQUEUE;
	@Override
	public ResponseBase findById(long userId) {
		UserEntity userEntity = memberDao.findByID(userId);
		System.out.println(userEntity);
		if (userEntity == null) {
			return setResultfail("找不到该用户");
		}
		return setResultSuccess(userEntity);
	}

	@Override
	public ResponseBase regUser(@RequestBody UserEntity user) {

		String username = user.getUsername();
		String password = user.getPassword();
		if (StringUtils.isEmpty(username)) {
			return setResultfail("用户名不能为空");
		}
		if (StringUtils.isEmpty(password)) {
			return setResultfail("密码不能为空");
		}
		String newPassword = MD5Util.MD5(password);
		user.setPassword(newPassword);
		user.setCreated(new Date());
		user.setUpdated(new Date());
		Integer result = memberDao.insertUser(user);
		if (result <= 0) {
			return setResultfail("用户注册失败");
		}
		//采用异步方式发送邮件
		String emailJson = emailJson(user.getEmail());
		log.info("消息服务平台发送邮件.....{}", emailJson);
		sendEmail(emailJson);
		return setResultSuccess("用户注册成功！");
	}
	
	private String emailJson(String email) {
		
		JSONObject rootJson = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("interfaceType", Constants.MSG_EMAIL);
		JSONObject content = new JSONObject();
		
		content.put("email", email);
		rootJson.put("header", header);
		rootJson.put("content", content);
		
		return rootJson.toJSONString();
	}
	
	private void sendEmail(String json) {
		log.info("开始发送邮件.....");
		ActiveMQQueue mqQueue = new ActiveMQQueue(MESSAGESQUEUE);
		registerMailboxProducer.sendMsg(mqQueue, json);
		log.info("发送邮件结束.....");
	}

	@Override
	public ResponseBase login(@RequestBody UserEntity user) {
		//1.验证参数
		String username = user.getUsername();
		String password = user.getPassword();
		
		if(StringUtils.isEmpty(username)) {
			return setResultfail("用户名不能为空！");
		}
		if(StringUtils.isEmpty(password)) {
			return setResultfail("密码不能为空！");
		}
		//2.查询用户是否存在,密码加密
		String newPassword=MD5Util.MD5(password);
		UserEntity userEntity = memberDao.login(username, newPassword);
		if(userEntity==null) {
			return setResultfail("用户名或密码错误！");
		}
		//3.如果用户存在，生成TOKEN
		String token = Tokenutils.getToken();
		//4.把TOKEN缓存到redis中，key为TOKEN，value为userID
		Integer userId = userEntity.getId();
		log.info("将信息存入到redis中....token为{},ID为{}",token,userId);
		baseRedisService.setString(token, userId+"", Constants.TOKEN_MEMBER_TIME);
		//5.返回TOKEN，json包装
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("memberToken", token);
		return setResultSuccess(jsonObject);
	}

	@Override
	public ResponseBase findByToken(@RequestParam("token") String token) {
		//1.查询token是否存在
		if(StringUtils.isEmpty(token)) {
			return setResultfail("token不能为空！");
		}
		//2.查找redis，通过token得到userId
		String struserId = (String) baseRedisService.getString(token);
		if(StringUtils.isEmpty(struserId)) {
			return setResultfail("token无效！");
		}
		
		//3.通过ID查询用户信息
		Long userId=Long.parseLong(struserId);
		UserEntity user = memberDao.findByID(userId);
		user.setPassword(null);
		return setResultSuccess(user);
	}
	
	private ResponseBase setLogin(UserEntity user) {
		String username = user.getUsername();
		String password = user.getPassword();
		
		if(StringUtils.isEmpty(username)) {
			return setResultfail("用户名不能为空！");
		}
		if(StringUtils.isEmpty(password)) {
			return setResultfail("密码不能为空！");
		}
		UserEntity userEntity = memberDao.login(username, password);
		if(userEntity==null) {
			return setResultfail("用户名或密码错误！");
		}
		//3.如果用户存在，生成TOKEN
		String token = Tokenutils.getToken();
		//4.把TOKEN缓存到redis中，key为TOKEN，value为userID
		Integer userId = userEntity.getId();
		log.info("将信息存入到redis中....token为{},ID为{}",token,userId);
		baseRedisService.setString(token, userId+"", Constants.TOKEN_MEMBER_TIME);
		//5.返回TOKEN，json包装
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("memberToken", token);
		return setResultSuccess(jsonObject);
	}

	@Override
	public ResponseBase findByOpenIdUser(@RequestParam("openId") String openId) {
		//1.验证参数
		if(StringUtils.isEmpty(openId)) {
			return setResultfail("openId为空！");
		}
		//2.根据openId查询用户信息
		UserEntity user = memberDao.findByOpenIdUser(openId);
		if(user==null) {
			return setResultfailOpenId(Constants.HTTP_RES_CODE_201, "用户没有关联账户");
		}
		//3.自动登录
		return setLogin(user);
	}

	@Override
	public ResponseBase qqLogin(@RequestBody UserEntity user) {
		//1.验证参数
		String openId = user.getOpenid();             
		if(StringUtils.isEmpty(openId)) {
			return setResultfail("openid为空！");
		}
		//2.进行账户登录
		ResponseBase responseBase = login(user);
		if(!responseBase.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
			return responseBase;
		}
		//3.如果登录成功
		//获取Token信息，修改openid到数据库
		JSONObject jsonObject = (JSONObject) responseBase.getData();
		String token = jsonObject.getString("memberToken");
		ResponseBase findByToken = findByToken(token);
		if(!findByToken.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
			return setResultfail("token无效");
		}
		UserEntity userEntity = (UserEntity) findByToken.getData();
		Integer userId = userEntity.getId();
		Integer updateUser = memberDao.updateUser(openId, userId);
		if(updateUser<=0) {
			setResultfail("QQ关联失败！");
		}
			
		return responseBase;
	}

}
