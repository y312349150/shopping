package com.my.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.base.ResponseBase;

@RequestMapping("/Order")
public interface OrderService {
	
	@RequestMapping("/updateOrder")
	public  ResponseBase updateOrder(@RequestParam("isPay") long isPay,@RequestParam("payId") String payId,@RequestParam("orderNumber") String orderNumber);
}
