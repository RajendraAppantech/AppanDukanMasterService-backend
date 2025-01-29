package com.appan;
 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.google.gson.Gson;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@Component
public class LoggingFilter implements Filter {
 
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private final Gson gson = new Gson();
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(wrappedRequest, wrappedResponse);
        logRequestResponse(wrappedRequest, wrappedResponse);
    }
 
    private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws IOException {
    	//logger.info("\r\n\r\n**************************** "+request.getRequestURI()+" *************************************");
        String requestHeaders = Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .reduce("", (acc, header) -> acc + header + ", ");
 
        String responseHeaders = response.getHeaderNames().stream()
                .map(headerName -> headerName + ": " + response.getHeader(headerName))
                .reduce("", (acc, header) -> acc + header + ", ");
 
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        String requestParams = Collections.list(request.getParameterNames()).stream()
                .map(paramName -> paramName + "=" + request.getParameter(paramName))
                .collect(Collectors.joining(", "));
 
 
        // Log request details
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Headers: {}", requestHeaders);
        logger.info("Request Parameters: {}", requestParams);
        logger.info("Request Body: {}", formatJson(requestBody)); // Format request body as JSON on a single line
 
        // Log response details
        logger.info("Response Status: {}", response.getStatus());
        logger.info("Response Headers: {}", responseHeaders);
        logger.info("Response Body: {}", formatJson(responseBody)); // Format response body as JSON on a single line
        response.copyBodyToResponse();
    }
 
    private String formatJson(String jsonString) {
        try {
            Object jsonObject = gson.fromJson(jsonString, Object.class);
            return gson.toJson(jsonObject);
        } catch (Exception e) {
            return jsonString; // If JSON parsing fails, return the original string
        }
    }
	/*@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*")); // Set allowed origins here
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Set allowed HTTP methods here
		configuration.setAllowedHeaders(Arrays.asList("*")); // Set allowed headers here
 
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
 
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}*/
}