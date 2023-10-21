package com.pyruz.shortening.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@PropertySource(name = "messages", value = "classpath:/messages.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@PropertySource(name = "application", value = "classpath:/application.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class ApplicationProperties {
    @Resource
    private Environment environment;

    public ApplicationProperties(Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String name) {
        return environment.getProperty(name);
    }
}
