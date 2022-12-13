package com.csc.spring.autoconfigure.tomcat.factory;

import com.csc.spring.autoconfigure.tomcat.TomcatProperties;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * @Description : 自定义策略接口，会在tomcat启动之后回调，会被WebServerFactoryEagleizerBeanPostProcessor类回调
 * @create: 2022/12/13
 */
public class EagleTomcatServer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    /**
     * 属性配置
     */
    private TomcatProperties properties;

    public EagleTomcatServer(TomcatProperties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(properties.getPort());
        //non-SSL to SSL（暂时不生效）
        connector.setRedirectPort(8081);
        factory.addAdditionalTomcatConnectors(connector);
    }
}
