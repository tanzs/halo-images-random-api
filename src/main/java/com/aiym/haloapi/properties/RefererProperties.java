package com.aiym.haloapi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tanzs
 * @date 2025/7/10 下午12:20
 * @description referer 白名单配置类
 */
@Component
@ConfigurationProperties(prefix = "referer")
public class RefererProperties {
    private List<String> allowList;

    public List<String> getAllowList() {
        return allowList;
    }

    // 支持字符串类型注入，自动转换为 List
    public void setAllowList(String allowListStr) {
        if (allowListStr != null) {
            this.allowList = Arrays.stream(allowListStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
    }

    public boolean isAllowAll() {
        return allowList != null && allowList.contains("*");
    }
}
