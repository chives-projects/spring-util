package com.csc.spring.httpclient;

import com.csc.spring.httpclient.annotation.TargetHttpTimeout;
import com.csc.spring.httpclient.factory.HttpContextFactory;
import com.csc.spring.httpclient.handler.CustomResponseErrorHandler;
import com.csc.spring.httpclient.interceptor.client.DefaultHttpClientInterceptor;
import com.csc.spring.httpclient.interceptor.client.HttpClientCustomizer;
import com.csc.spring.httpclient.interceptor.timeout.DefaultHttpTimeoutMethodInterceptor;
import com.csc.spring.httpclient.interceptor.timeout.HttpTimeoutCustomizer;
import com.csc.spring.logback.LoggerFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * @author: csc
 * @Description: 将RestTemplate加入容器
 */
@AutoConfiguration
@ConditionalOnClass(RestTemplate.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class HttpClientAutoConfiguration implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientAutoConfiguration.class);

    /**
     * 将RestTemplate加入容器，对异常处理进行处理，使异常也可以返回结果
     */
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RestTemplate restTemplate(ObjectProvider<HttpClientCustomizer> httpClientCustomizers, ClientHttpRequestFactory clientHttpRequestFactory, HttpClientProperties httpClientProperties) {
        RestTemplate restTemplate = new RestTemplate();
        //设置BufferingClientHttpRequestFactory将输入流和输出流保存到内存中，允许多次读取;类似io流的装饰器模式
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(clientHttpRequestFactory));
        //设置自定义异常处理
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        if (httpClientProperties.isInterceptor()) {
            //添加拦截器
            restTemplate.setInterceptors(Collections.singletonList(httpClientCustomizers.orderedStream().findFirst().get()));
        }

        return restTemplate;
    }

    /**
     * 定义HTTP请求工厂方法,设置超市时间
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClientProperties properties) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //读取超时5秒,默认无限限制,单位：毫秒
        factory.setReadTimeout(properties.getReadTimeOut());
        //连接超时10秒，默认无限制，单位：毫秒
        factory.setConnectTimeout(properties.getConnectTimeOut());
        //设置HTTP进程执行状态工厂类
        factory.setHttpContextFactory(new HttpContextFactory());
        //开启HTTPS请求支持
        if (properties.isSsl()) {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            factory.setHttpClient(HttpClientBuilder.create().setSSLSocketFactory(connectionSocketFactory).build());
        }
        return factory;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public DefaultHttpClientInterceptor httpClientInterceptor() {
        return new DefaultHttpClientInterceptor();
    }

    /**
     * RestTemplate请求超时切面（单个请求）
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor httpTimeoutPointCutAdvice(ObjectProvider<HttpTimeoutCustomizer> httpTimeoutCustomizers) {
        //限定方法级别的切点
        Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(TargetHttpTimeout.class);
//        Pointcut mpc = new AnnotationMatchingPointcut(null, TargetHttpTimeout.class, false);
        //组合切面(并集)，即只要有一个切点的条件符合，则就拦截
//        pointcut = new ComposablePointcut(pointcut);
        //切面增强类
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(httpTimeoutCustomizers.orderedStream().findFirst().get());
//        AnnotationPointcutAdvisor advisor = new AnnotationPointcutAdvisor(customizers.orderedStream().findFirst().get(), pointcut);
        //切面优先级顺序
        advisor.setOrder(AopOrderInfo.HTTP_CLIENT);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public HttpTimeoutCustomizer httpTimeoutCustomizer() {
        return new DefaultHttpTimeoutMethodInterceptor();
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----RestTemplate(HttpClient)组件【HttpClientAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----RestTemplate(HttpClient)组件【HttpClientAutoConfiguration】");
    }
}
