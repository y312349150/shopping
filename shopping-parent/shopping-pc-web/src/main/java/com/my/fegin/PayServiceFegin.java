package com.my.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.my.service.PayService;

@Component
@FeignClient("pay")
public interface PayServiceFegin extends PayService{

}
