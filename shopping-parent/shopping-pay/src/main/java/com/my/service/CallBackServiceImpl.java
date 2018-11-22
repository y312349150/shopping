package com.my.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import com.my.base.BaseApiService;
import com.my.base.ResponseBase;
import com.my.constants.Constants;
import com.my.dao.PaymentInfoDao;
import com.my.entity.PaymentInfo;
import com.my.fegin.OrderFegin;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class CallBackServiceImpl extends BaseApiService implements CallBackService{
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private OrderFegin orderFegin;
	@Override
	public ResponseBase synCallBack(@RequestParam Map<String,String> params) {
		//1.打印日志
		log.info("------------同步通知开始-----------params:{}",params);
		//2.参数验签
		try {
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
			if(!signVerified) {
				return setResultfail("验签失败！");
			}
			//商户订单号
			String outTradeNo = params.get("out_trade_no");
			//支付宝交易号
			String tradeNo = params.get("trade_no");
			//付款金额
			String totalAmount = params.get("total_amount");
			JSONObject data = new JSONObject();
			data.put("outTradeNo", outTradeNo);
			data.put("tradeNo", tradeNo);
			data.put("totalAmount", totalAmount);
			return setResultSuccess(data);
		} catch (Exception e) {
			log.error("支付宝同步通知出现异常:{}",e);
			return setResultfail("系统错误");
		}finally {
			log.info("------------同步通知结束-----------params:{}",params);
		}
		
	}

	@Override
	public String asynCallBack(@RequestParam Map<String,String> params) {
		//1.打印日志
				log.info("------------异步通知开始-----------params:{}",params);
				//2.参数验签
				try {
					boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
					if(!signVerified) {
						return Constants.PAY_FAIL;
					}
					//商户订单号
					String outTradeNo = params.get("out_trade_no");
					//处理幂等
					PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(outTradeNo);
					if (paymentInfo == null) {
						return Constants.PAY_FAIL;
					}
					//查看支付状态
					Integer state = paymentInfo.getState();
					//如果已经支付成功，不再进行重试，防止重复
					if(state==1) {
						return Constants.PAY_SUCCESS;
					}
					//支付宝交易号
					String tradeNo = params.get("trade_no");
					//付款金额
					String totalAmount = params.get("total_amount");
					//修改支付状态
					paymentInfo.setState(1);
					paymentInfo.setPayMessage(params.toString());
					paymentInfo.setPlatformorderId(tradeNo);
					//
					Integer info = paymentInfoDao.updatePayInfo(paymentInfo);
					if(info<=0) {
						return Constants.PAY_FAIL;
					}
					//调用订单接口进行通知
					ResponseBase updateOrder = orderFegin.updateOrder(1l, tradeNo, outTradeNo);
					if(!updateOrder.getReturnCode().equals(Constants.HTTP_RES_CODE_200)) {
						return Constants.PAY_FAIL;
					}
					return Constants.PAY_SUCCESS;
				} catch (Exception e) {
					log.error("支付宝异步通知出现异常:{}",e);
					return Constants.PAY_FAIL;
				}finally {
					log.info("------------异步通知结束-----------params:{}",params);
				}
				
			}
	

}
