package com.appan;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RequestHeaderFilter  /*extends OncePerRequestFilter */{

	private static final Logger logger = LoggerFactory.getLogger(RequestHeaderFilter.class);

	@Value("${TOKEN_URL}")
	private String tokenUrl;

	private String username = null;

	@Autowired
	private UserMasterRepository masterRepository;

/*	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String requestHeaders = Collections.list(request.getHeaderNames()).stream()
				.map(headerName -> headerName + ": " + request.getHeader(headerName))
				.reduce("", (acc, header) -> acc + header + ", ");
		try {

			if (!requestHeaders.contains("authtoken") || !requestHeaders.contains("session-id")) {
				logger.error("Invalid session: {}", "Invalid session id");
				CommonResponse resp1 = new CommonResponse();
				resp1.setStatus(false);
				resp1.setMessage("missing authorization header");
				resp1.setRespCode("401");
				ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("" + resp1);
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(errorResponse.getStatusCodeValue());
				httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				byte[] responseBody = errorResponse.getBody().getBytes();
				httpResponse.setContentLength(responseBody.length);
				httpResponse.getOutputStream().write(responseBody);
				return;
			}

			String authHeader = request.getHeader("authtoken").trim();
			String sessionId = request.getHeader("session-id").trim();

			logger.info("authHeader: {}", authHeader, "session-id: {}", sessionId);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.set("Content-Type", "application/json");
			headers.set("authtoken", authHeader);
			RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, null);
			ResponseEntity<String> resp = new RestTemplate().exchange(tokenUrl + "validate", HttpMethod.GET,
					requestEntity, String.class);
			username = resp.getBody();

			HttpHeaders respHeaders = resp.getHeaders();
			logger.info("Response From Client: {}", resp);
			logger.info("Response From Client: {}", username);
			String newToken = respHeaders.get("authtoken").get(0);
			response.setHeader("authtoken", newToken);

			UserMaster master = masterRepository.findByUserId(username);
			if (!master.getSessionId().equalsIgnoreCase(sessionId)) {
				logger.error("Invalid session: {}", "Invalid session id");
				CommonResponse resp1 = new CommonResponse();
				resp1.setStatus(false);
				resp1.setMessage("Invalid session");
				resp1.setRespCode("401");
				ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("" + resp1);
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(errorResponse.getStatusCodeValue());
				httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				byte[] responseBody = errorResponse.getBody().getBytes();
				httpResponse.setContentLength(responseBody.length);
				httpResponse.getOutputStream().write(responseBody);
				return;
			}

			chain.doFilter(request, response);
			logger.info("Request processing completed");
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				logger.error("Unauthorized access: {}", e.getMessage());
				CommonResponse resp = new CommonResponse();
				resp.setStatus(false);
				resp.setMessage("Token is expired");
				resp.setRespCode("401");
				ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("" + resp);
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(errorResponse.getStatusCodeValue());
				httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				byte[] responseBody = errorResponse.getBody().getBytes();
				httpResponse.setContentLength(responseBody.length);
				httpResponse.getOutputStream().write(responseBody);
			} else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
				logger.error("Unauthorized access: {}", e.getMessage());
				CommonResponse resp = new CommonResponse();
				resp.setStatus(false);
				resp.setMessage("Invalid Token");
				resp.setRespCode("403");
				ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).body("" + resp);
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(errorResponse.getStatusCodeValue());
				httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				byte[] responseBody = errorResponse.getBody().getBytes();
				httpResponse.setContentLength(responseBody.length);
				httpResponse.getOutputStream().write(responseBody);
			} else {
				logger.error("Error in logging filter", e);
				ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error processing request: " + e.getMessage());
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(errorResponse.getStatusCodeValue());
				httpResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);
				byte[] responseBody = errorResponse.getBody().getBytes();
				httpResponse.setContentLength(responseBody.length);
				httpResponse.getOutputStream().write(responseBody);
			}
		} catch (RuntimeException e) {
			logger.error("Error in logging filter", e);
			ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing request: " + e.getMessage());
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(errorResponse.getStatusCodeValue());
			httpResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);
			byte[] responseBody = errorResponse.getBody().getBytes();
			httpResponse.setContentLength(responseBody.length);
			httpResponse.getOutputStream().write(responseBody);
		}
	}
	*/
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));// Set allowed origins pattern here
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));// Set allowed HTTP methods here
		configuration.setAllowedHeaders(
				Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "authtoken"));

		// This allow us to expose the headers
		configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers",
				"Hash, Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
						+ "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authtoken"));
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
	}
}