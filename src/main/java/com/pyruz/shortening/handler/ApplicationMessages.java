package com.pyruz.shortening.handler;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationMessages {
    private static final ApplicationMessages instance = new ApplicationMessages();
    private static java.util.Properties properties;
    String prop = "messages.properties";

    private ApplicationMessages() {
        properties = new java.util.Properties();
    }

    public static ApplicationMessages getInstance() {
        return instance;
    }

    public String getProperty(String key) {
        try {
            properties.load(ApplicationMessages.class.getClassLoader().getResourceAsStream(prop));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

}
