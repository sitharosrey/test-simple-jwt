package com.example.pvhjwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            if (!response.isCommitted()) {

                int status = resolveHttpStatus(ex);

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", Instant.now().toString());
                body.put("status", status);
                body.put("error", getErrorTitle(ex));
                body.put("message", getErrorMessage(ex));
                body.put("path", request.getRequestURI());

                response.setStatus(status);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getOutputStream(), body);
            } else {
                throw ex;
            }
        }
    }

    private int resolveHttpStatus(Exception ex) {
        if (ex instanceof org.springframework.dao.DataAccessException) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else if (ex instanceof IllegalArgumentException ||
                   ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            return HttpStatus.BAD_REQUEST.value();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    private String getErrorTitle(Exception ex) {
        if (ex instanceof org.springframework.dao.DataAccessException) {
            return "Database Error";
        } else if (ex instanceof IllegalArgumentException) {
            return "Invalid Request";
        } else if (ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            return "Missing Parameter";
        } else {
            return "Unexpected Error";
        }
    }

    private String getErrorMessage(Exception ex) {
        if (ex instanceof org.springframework.dao.DataAccessException) {
            return "A database error occurred. Please try again later.";
        } else if (ex instanceof IllegalArgumentException ||
                   ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            return ex.getMessage();
        } else {
            return "Something went wrong. Please try again later.";
        }
    }
}