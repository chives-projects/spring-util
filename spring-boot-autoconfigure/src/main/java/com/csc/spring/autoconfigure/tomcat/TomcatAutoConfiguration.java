package com.csc.spring.autoconfigure.tomcat;

import com.csc.spring.autoconfigure.InitializingAutoConfig;
import com.csc.spring.autoconfigure.tomcat.factory.EagleTomcatServer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HTTP自动跳转HTTPS自动化配置类
 * 生成证书命令：
 * keytool -genkey -alias michaelSpica  -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore /Users/csc/Documents/IDE/workplace/security/keystore.p12 -validity 3650 -dname "CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN"
 *
 * @author: csc
 */
@AutoConfiguration
@EnableConfigurationProperties(TomcatProperties.class)
@ConditionalOnProperty(prefix = "server.http", name = "enabled", havingValue = "true", matchIfMissing = false)
public class TomcatAutoConfiguration implements InitializingAutoConfig {

    @Bean
    public EagleTomcatServer tomcatServerEagleizer(TomcatProperties properties) {
        return new EagleTomcatServer(properties);
    }
}