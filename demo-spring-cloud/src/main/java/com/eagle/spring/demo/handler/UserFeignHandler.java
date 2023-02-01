package com.eagle.spring.demo.handler;

import com.eagle.common.po.BaseResponse;
import com.eagle.spring.demo.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "custom", url = "http://127.0.0.1:8080/api/feign", contextId = "custom")
public interface UserFeignHandler {
    /**
     * 自定义超时请求
     */
    @PostMapping("custom")
    BaseResponse<User> getUser(User user);
}
