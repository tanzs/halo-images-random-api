package com.example.haloapi.controller;

import com.example.haloapi.domain.ApiResponse;
import com.example.haloapi.service.RandomImageService;
import com.example.haloapi.util.IpRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
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

    @GetMapping(value = "/images")
    public ResponseEntity<?> getImage(@RequestParam(required = false) String postid,
                                           HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        if (ipRateLimiter.isLimited(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.of(429, "Too frequent"));
        }

        if (postid == null || postid.isBlank()) {
            postid = UUID.randomUUID().toString();
        }

        try {
            String url = imageService.getRandomImage(postid);
            if (url == null || url.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.of(404, "No image found"));
            }

            // 成功获取图片 URL，302 重定向
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.of(500, "Server error"));
        }
    }

    @GetMapping(value = "/images-random")
    public ResponseEntity<?> getImageRandom(@RequestParam(required = false) String postid, @RequestParam String key) {
        if (key == null || !authKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.of(401, "Unauthorized"));
        }


        if (postid == null || postid.isBlank()) {
            postid = UUID.randomUUID().toString();
        }

        try {
            String url = imageService.getRandomImage(postid);
            if (url == null || url.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.of(404, "No image found"));
            }

            // 成功获取图片 URL，302 重定向
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.of(500, "Server error"));
        }
    }
}
