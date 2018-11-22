package com.my.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.my.constants.Constants;

@Component
public class BaseApiService {
	@Autowired
	protected BaseRedisService baseRedisService;

	// 访问失败,可以传msg
	public ResponseBase setResultfail(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}
	// 访问失败,可以传msg
	public ResponseBase setResultfailOpenId(Integer code,String msg) {
		return setResult(code, msg, null);
	}

	// 成功访问，传数据
	public ResponseBase setResultSuccess(Object data) {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
	}

	// 成功访问，但不传数据
	public ResponseBase setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
	}

	// 成功访问，返回信息
	public ResponseBase setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, null);
	}

	// 通用方法
	public ResponseBase setResult(Integer returnCode, String msg, Object data) {
		return new ResponseBase(returnCode, msg, data);
	}
}
