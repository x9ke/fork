package com.du.besttrip.ordersb2c.util;

import com.du.besttrip.ordersb2c.constant.MDCParam;
import lombok.experimental.UtilityClass;
import org.jboss.logging.MDC;

import java.util.UUID;
import java.util.function.Function;

@UtilityClass
public class MdcUtil {

    private static final Function<String, UUID> PARSE_UUID = uuidString -> {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format in MDC: " + uuidString, e);
        }
    };

    public static UUID getUserId() {
        String userId = (String) MDC.get(MDCParam.USER_ID.name());
        if (userId == null) {
            throw new IllegalStateException("USER_ID not present in MDC");
        }
        return PARSE_UUID.apply(userId);
    }

    public static UUID getRequestId() {
        String requestId = (String) MDC.get(MDCParam.REQUEST_ID.name());
        if (requestId == null) {
            throw new IllegalStateException("REQUEST_ID not present in MDC");
        }
        return PARSE_UUID.apply(requestId);
    }
}

