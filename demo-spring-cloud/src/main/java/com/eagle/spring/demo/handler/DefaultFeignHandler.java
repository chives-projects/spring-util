package com.eagle.spring.demo.handler;

import com.eagle.common.po.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "connect", url = "http://127.0.0.1:8080/api/feign")
public interface DefaultFeignHandler {
    /**
     * 默认超时请求
     */
    @GetMapping("connect")
    BaseResponse<String> getConnect(@RequestParam("timeout") int timeout);
}
