package com.my.constants;
public interface Constants {

	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "success";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "fial";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 
	Integer HTTP_RES_CODE_201 = 201;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;
	// 发送类型为邮件
	String MSG_EMAIL = "email";
	//支付成功
	String PAY_SUCCESS = "success";
	//支付失败
	String PAY_FAIL = "fail";
	//TOKEN
	String TOKEN_MEMBER = "TOKEN_MEMBER";
	String TOKEN_PAY = "TOKEN_PAY";
	//缓存时间
	long TOKEN_MEMBER_TIME=(long)(60*60*24*90);
	int LOGIN_TOKEN_MEMBER_TIME=60*60*24*90;
	long PAY_TOKEN_TIME=(long)60*15;
	
	//Cookie会员Token
	String COOKIE_TOKEN_MEMBER = "COOKIE_TOKEN_MEMBER";
	
}