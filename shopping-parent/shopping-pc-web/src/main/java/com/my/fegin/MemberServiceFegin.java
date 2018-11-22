package com.my.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.my.api.service.MemberService;
@FeignClient("member")
@Component
public interface MemberServiceFegin extends MemberService {

}
