package com.aiym.haloapi.handler;

import com.aiym.haloapi.annotation.CheckReferer;
import com.aiym.haloapi.util.RefererValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Tanzs
 * @date 2025/7/10 下午12:18
 * @description Referer拦截器
 */
@Component
public class RefererInterceptor implements HandlerInterceptor {
    private final RefererValidator refererValidator;

    public RefererInterceptor(RefererValidator refererValidator) {
        this.refererValidator = refererValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只拦截使用了 @CheckReferer 注解的方法或类
        if (handler instanceof HandlerMethod handlerMethod) {
            boolean hasAnnotation = handlerMethod.getMethodAnnotation(CheckReferer.class) != null ||
                    handlerMethod.getBeanType().getAnnotation(CheckReferer.class) != null;

            if (hasAnnotation && !refererValidator.isValid(request)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"Forbidden: invalid referer\"}");
                return false;
            }
        }

        return true;
    }
}
