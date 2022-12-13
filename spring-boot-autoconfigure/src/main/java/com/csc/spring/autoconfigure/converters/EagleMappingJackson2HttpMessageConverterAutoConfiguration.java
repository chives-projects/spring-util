package com.csc.spring.autoconfigure.converters;

import com.csc.spring.autoconfigure.InitializingAutoConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author: csc
 * @description: springboot框架将字典数据类型转换为json，Content-Type默认由 content-type: application/json 更改为：content-type: application/json;charset=UTF-8
 * @create: 2022/12/13
 */
@AutoConfiguration(after = HttpMessageConvertersAutoConfiguration.class)
@EnableConfigurationProperties(Jackson2MessagesProperties.class)
@ConditionalOnProperty(prefix = "spring.csc.jackson2.converter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EagleMappingJackson2HttpMessageConverterAutoConfiguration implements InitializingAutoConfig {

    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public EagleMappingJackson2HttpMessageConverterAutoConfiguration(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @PostConstruct
    public void EagleMappingJackson2HttpMessageConverterAutoConfiguration() {
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
    }
}
