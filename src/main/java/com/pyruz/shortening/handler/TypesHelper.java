package com.pyruz.shortening.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Calendar;
import java.util.Date;

public class TypesHelper {

    private static final String BENZ = "BENZ";
    private static final String UNKNOWN = "UNKNOWN";
    protected static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-",
            "X-Forwarded-For",
            "X-Forwarded-Host",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED_Host",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private TypesHelper() {
    }

    public static Integer tryParseInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    public static String getHashCodeString(int hashCode) {
        char[] benz = BENZ.toCharArray();
        char[] temp = String.format("%06d", hashCode).toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : temp) {
            int num = Integer.parseInt(String.valueOf(c));
            builder.append(num > 3 ? '0' : benz[num]);
        }
        return builder.toString();
    }

    public static String getClientIpAddress() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            for (String header : HEADERS_TO_TRY) {
                String ip = request.getHeader(header);
                if (ip != null && !ip.isEmpty() && !UNKNOWN.toLowerCase().equalsIgnoreCase(ip)) {
                    return ip;
                }
            }
            return request.getRemoteAddr();
        }
        return UNKNOWN;
    }

    private static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    public static Date yesterdayStartDatetime() {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DAY_OF_MONTH, -1);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        return from.getTime();
    }

    public static Date yesterdayEndDatetime() {
        Calendar to = Calendar.getInstance();
        to.add(Calendar.DAY_OF_MONTH, -1);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        to.set(Calendar.MILLISECOND, 999);
        return to.getTime();
    }


}
