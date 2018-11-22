package com.my.service;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.my.base.BaseApiService;
import com.my.base.BaseRedisService;
import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.dao.PaymentInfoDao;
import com.my.entity.PaymentInfo;
import com.my.utils.Tokenutils;

@RestController
public class PayServiceImpl extends BaseApiService implements PayService {
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private BaseRedisService baseRedisService;
	@Override
	//创建支付令牌
	public ResponseBase createPayToken(@RequestBody PaymentInfo paymentInfo) {
		//1.创建支付请求信息
		Integer paymentType = paymentInfoDao.savePaymentType(paymentInfo);
		if(paymentType<=0) {
			return setResultfail("创建支付请求失败！");
		}
		//2.生成Token
		String payToken = Tokenutils.getPayToken();
		//3.将Token存入到Redis中，key为Token，value为支付表自增ID
		Integer id = paymentInfo.getId();
		System.out.println(payToken+"......."+id);
		baseRedisService.setString(payToken, id+"", Constants.PAY_TOKEN_TIME);
		//4.返回Token
		JSONObject data = new JSONObject();
		data.put("payToken", payToken);
		return setResultSuccess(data);
	}

	@Override
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken) {
		//1.验证参数
		if(StringUtils.isEmpty(payToken)) {
			return setResultfail("Token不能为空");
		}
		//2.判断Token有效期
		//3.使用Token再Redis中查找支付ID
		String payId = (String) baseRedisService.getString(payToken);
		if(StringUtils.isEmpty(payId)) {
			return setResultfail("支付请求已经失效");
		}
		//4.使用支付ID下单
		long payIdl = Long.parseLong(payId);
		
		//5.通过支付ID查询得到订单信息
		PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(payIdl);
		if(paymentInfo==null) {
			return setResultfail("未找到支付信息");
		}
		//6.对接支付代码
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = paymentInfo.getOrderId();
		//付款金额，必填
		String total_amount = paymentInfo.getPrice()+"";
		//订单名称，必填
		String subject = "商品订单名称";
		//商品描述，可空
		//String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
//				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		try {
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			JSONObject data = new JSONObject();
			data.put("payHtml", result);
			return setResultSuccess(data);
		} catch (AlipayApiException e) {
			return setResultfail("支付接口异常");
		}
	
		
		
	}

}
