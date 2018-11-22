package com.my.emailService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.my.adapter.MessageAdapter;

import lombok.extern.slf4j.Slf4j;
//处理第三方发送邮件
@Slf4j
@Service
public class EmailService implements MessageAdapter{
	@Value("${msg.subject}")
	private String subject;
	@Value("${msg.text}")
	private String text;
	@Autowired
	JavaMailSender javaMailSender;

	@Override
	public void sendMsg(JSONObject body) {
		String email = body.getString("email");
		if(StringUtils.isEmpty(email)) {
			return;
		}
		
		log.info("发送邮件........... 邮件为:{}",email);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		//来自账号
		simpleMailMessage.setFrom(email);
		//发送到的账号
		simpleMailMessage.setTo(email);
		//发送的标题
		simpleMailMessage.setSubject(subject);
		//发送的内容
		simpleMailMessage.setText(text.replace("{}", email));
		
		javaMailSender.send(simpleMailMessage);
		
	}

}
