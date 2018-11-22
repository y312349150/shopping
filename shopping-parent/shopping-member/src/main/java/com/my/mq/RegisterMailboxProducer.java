package com.my.mq;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisterMailboxProducer {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	//传队列与json
	public void sendMsg(Destination destination, String json) {
		jmsMessagingTemplate.convertAndSend(destination, json);
	}
}
