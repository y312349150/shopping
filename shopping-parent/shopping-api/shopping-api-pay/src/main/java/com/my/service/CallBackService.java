package com.my.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.base.ResponseBase;

@RequestMapping("/CallBack")
public interface CallBackService {
	//同步通知
	@RequestMapping("/synCallBack")
	public ResponseBase synCallBack(@RequestParam Map<String,String> params);
	//异步通知
	@RequestMapping("/asynCallBack")
	public String asynCallBack(@RequestParam Map<String,String> params);

}
