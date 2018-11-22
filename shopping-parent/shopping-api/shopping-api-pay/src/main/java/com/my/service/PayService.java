package com.my.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.base.ResponseBase;
import com.my.entity.PaymentInfo;

@RequestMapping("/pay")
public interface PayService {
	//创建支付令牌Token
	@RequestMapping("/createPayToken")
	public ResponseBase createPayToken(@RequestBody PaymentInfo paymentInfo);
	//通过支付令牌查找信息
	@RequestMapping("/findPayToken")
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken);
}
