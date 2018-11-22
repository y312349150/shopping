package com.my.mq;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.my.adapter.MessageAdapter;
import com.my.constants.Constants;
import com.my.emailService.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerDistribute {
	
	@Autowired
	private EmailService emailService;
	private MessageAdapter messageAdapter;
	
	@JmsListener(destination="messages_queue")
	public void distribute(String json) {
		log.info("####ConsumerDistribute###distribute() 消息服务平台接受 json参数:" + json);
		JSONObject jsonObecjt = new JSONObject().parseObject(json);
		JSONObject header = jsonObecjt.getJSONObject("header");
		String interfaceType = header.getString("interfaceType");
		if(StringUtils.isEmpty(interfaceType)) {
			return;
		}
		//判断接口类型是否为邮件
		if(interfaceType.equals(Constants.MSG_EMAIL)) {
			//调用邮件接口
			messageAdapter=emailService;
		}
		if(messageAdapter==null) {
			return;
		}
		JSONObject body = jsonObecjt.getJSONObject("content");
		messageAdapter.sendMsg(body);
		log.info("body.......:{}",body);
		
		
	}
}