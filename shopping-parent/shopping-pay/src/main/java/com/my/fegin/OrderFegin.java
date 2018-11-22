package com.my.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.my.service.OrderService;
@Component
@FeignClient("order")
public interface OrderFegin extends OrderService {

}
