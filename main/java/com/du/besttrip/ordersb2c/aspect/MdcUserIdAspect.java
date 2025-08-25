package com.du.besttrip.ordersb2c.aspect;

import com.du.besttrip.ordersb2c.constant.MDCParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Parameter;

@Aspect
@Component
public class MdcUserIdAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object addUserIdToMdc(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = methodSignature.getMethod().getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
            if (requestHeader != null) {
                if ("X-User-Id".equals(requestHeader.value()) && args[i] instanceof String) {
                    MDC.put(MDCParam.USER_ID.name(), (String) args[i]);
                }
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            MDC.remove(MDCParam.USER_ID.name());
        }
    }
}
