package com.pyruz.shortening.handler;

import com.pyruz.shortening.model.entiry.Url;
import lombok.SneakyThrows;
import java.net.URL;

public class UrlHandler {

    private UrlHandler() {
    }

    @SneakyThrows
    public static String getBaseDomain(String url) {
        URL aURL = new URL(url);
        String host = aURL.getHost();
        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            host = host.substring(startIndex);
        }
        if (host.contains(".")) {
            host = host.split("\\.")[0];
        }
        if (host.contains("-")) {
            host = host.substring(0, host.indexOf('-'));
        }
        return aURL.getProtocol() + "://" + host + ".de";
    }

    public static String getShortPortion(String shortURL) {
        return shortURL.substring(shortURL.lastIndexOf('/') + 1);
    }

    public static String getFullShorURL(Url url) {
        return UrlHandler.getBaseDomain(url.getOriginalURL()) + "/" + url.getShortURL();
    }
}
