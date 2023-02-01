package com.eagle.spring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * -DCONSUL_HOST=xxx.xxx.xxx.xxx -DJAVA_LOCAL_MANAGEMENT_PORT=8180 -DJAVA_LOCAL_PORT=8080 -DJAVA_LOCAL_IP=xxx.xxx.xxx.xxx
 */
@SpringBootApplication
@EnableFeignClients
public class DemoSpringCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringCloudApplication.class, args);
    }

}
