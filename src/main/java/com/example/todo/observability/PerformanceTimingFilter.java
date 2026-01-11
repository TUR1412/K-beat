package com.example.todo.observability;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class PerformanceTimingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(PerformanceTimingFilter.class);

    private final long slowRequestThresholdMs;
    private final boolean serverTimingEnabled;

    public PerformanceTimingFilter(
            @Value("${app.http.slowRequestThresholdMs:400}") long slowRequestThresholdMs,
            @Value("${app.http.serverTiming.enabled:true}") boolean serverTimingEnabled) {
        this.slowRequestThresholdMs = slowRequestThresholdMs;
        this.serverTimingEnabled = serverTimingEnabled;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startNs = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            if (serverTimingEnabled) {
                response.addHeader("Server-Timing", "app;dur=" + durationMs);
            }
            if (durationMs >= slowRequestThresholdMs) {
                log.warn(
                        "Slow request: method={} path={} status={} durationMs={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        durationMs);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path == null) {
            return false;
        }
        return path.startsWith("/assets/");
    }
}

