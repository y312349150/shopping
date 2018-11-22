package com.my.utils;

import java.util.UUID;

import com.my.constants.Constants;

public class Tokenutils {
	//生成登录令牌
	public static String getToken() {
		return Constants.TOKEN_MEMBER+UUID.randomUUID();
	}
	//生成支付令牌
	public static String getPayToken() {
		return Constants.TOKEN_PAY+UUID.randomUUID();
	}
}
