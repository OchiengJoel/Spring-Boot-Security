package com.joe.springsec.auth.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr(); // Get client IP address

        // Create or fetch the rate limit bucket for the IP
        Bucket bucket = cache.computeIfAbsent(ip, this::newBucket);

        // Try to consume a token
        if (bucket.tryConsume(1)) {
            return true;  // Allow request if there's a token available
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429 Too Many Requests
            response.getWriter().write("Too many login attempts. Try again later.");
            return false;  // Block request if limit exceeded
        }
    }

    private Bucket newBucket(String key) {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1)); // 5 tokens per minute
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}
