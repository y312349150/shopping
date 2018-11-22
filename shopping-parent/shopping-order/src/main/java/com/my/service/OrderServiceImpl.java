package com.my.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.my.base.BaseApiService;
import com.my.base.ResponseBase;
import com.my.dao.OrderDao;
@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService{
	@Autowired
	private OrderDao orderDao;
	@Override
	public ResponseBase updateOrder(long isPay, String payId, String orderNumber) {
		int updateOrder = orderDao.updateOrder(isPay, payId, orderNumber);
		if(updateOrder<=0) {
			setResultfail("订单更新错误");
		}
		return setResultSuccess();
	}

}
