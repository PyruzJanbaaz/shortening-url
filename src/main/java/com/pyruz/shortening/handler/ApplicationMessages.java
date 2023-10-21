package com.pyruz.shortening.handler;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class ApplicationMessages {
    private final Properties properties;

    public ApplicationMessages(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key) {
        try {
            String prop = "messages.properties";
            properties.load(ApplicationMessages.class.getClassLoader().getResourceAsStream(prop));
            return properties.getProperty(key);
        } catch (IOException ioException) {
            ioException.getMessage();
            return null;
        }
    }

}
