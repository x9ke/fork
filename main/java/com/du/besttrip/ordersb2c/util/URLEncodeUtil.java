package com.du.besttrip.ordersb2c.util;

import lombok.experimental.UtilityClass;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@UtilityClass
public final class URLEncodeUtil {
    public static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }
}
