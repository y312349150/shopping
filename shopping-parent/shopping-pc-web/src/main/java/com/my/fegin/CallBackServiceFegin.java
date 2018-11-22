package com.my.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.my.service.CallBackService;

@FeignClient("pay")
@Component
public interface CallBackServiceFegin extends CallBackService {

}
