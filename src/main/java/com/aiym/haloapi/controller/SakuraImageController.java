package com.aiym.haloapi.controller;

import com.aiym.haloapi.annotation.CheckReferer;
import com.aiym.haloapi.domain.ApiResponse;
import com.aiym.haloapi.service.RandomImageService;
import com.aiym.haloapi.util.IpRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SakuraImageController {

    @Autowired
    private RandomImageService imageService;

    @Autowired
    private IpRateLimiter ipRateLimiter;

    @Value("${auth.key}")
    private String authKey;

    @CheckReferer
    @GetMapping(value = "/images-random")
    public ResponseEntity<?> getImageRandom(@RequestParam String postid, @RequestParam String key, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        // 校验 key（如果 key 不匹配，返回 401）
        if (!authKey.equals(key)) {
            return buildResponse(401, "Unauthorized");
        }

        // 校验 User-Agent
        if (userAgent != null &&
                (userAgent.toLowerCase().contains("curl") || userAgent.toLowerCase().contains("python"))) {
            return buildResponse(403, "Forbidden: invalid client");
        }


        // 判断 postid 格式
        if (!postid.isBlank()) {
            try {
                UUID.fromString(postid);
            } catch (IllegalArgumentException e) {
                return buildResponse(400, "Invalid postid format");
            }
        }

        // 检查 IP 请求频率限制
        if (ipRateLimiter.isLimited(ip)) {
            return buildResponse(429, "Too frequent");
        }

        try {
            String url = imageService.getRandomImage(postid);
            if (url == null || url.isBlank()) {
                return buildResponse(404, "No image found");
            }

            // 成功获取图片 URL，302 重定向
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            return buildResponse(500, "Server error");
        }
    }

    // 封装返回结果的方法
    private ResponseEntity<ApiResponse> buildResponse(int code, String message) {
        ApiResponse response = ApiResponse.of(code, message);
        response.setCode(code);
        response.setMessage(message);
        return ResponseEntity.status(code).body(response);
    }
}
