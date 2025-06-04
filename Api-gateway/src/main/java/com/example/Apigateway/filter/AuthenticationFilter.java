package com.example.Apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.example.Apigateway.util.JwtUtil;
import com.google.common.net.HttpHeaders;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil util;

    public static class Config {
    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return handleUnauthorized(exchange.getResponse(), "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    String role = util.extractRolesFromToken(authHeader);
                    String requestedPath = exchange.getRequest().getPath().toString();
                    String method = exchange.getRequest().getMethod().name();

                    if (!isAuthorized(role, requestedPath, method)) {
                        return handleUnauthorized(exchange.getResponse(), "Unauthorized access");
                    }

                } catch (Exception e) {
                    return handleUnauthorized(exchange.getResponse(), "Invalid token");
                }
            }
            return chain.filter(exchange);
        };
    }

    private boolean isAuthorized(String role, String path, String method) {
		if ("MANAGER".equalsIgnoreCase(role)) {
			return path.startsWith("/attendance")||path.startsWith("/report") || path.startsWith("/employees")||path.startsWith("/balance/update")
					||path.startsWith("/balance/initialize")||path.startsWith("/balance/delete")
					|| path.startsWith("/leave/approve")
					|| path.startsWith("/leave/getAll") || path.startsWith("/leave/reject")||path.startsWith("/leave/employee")
					|| path.startsWith("/shifts");
		} else if ("EMPLOYEE".equalsIgnoreCase(role)) {
			return path.startsWith("/attendance")||path.startsWith("/employees")
					|| path.startsWith("/leave/apply")||path.startsWith("/leave/delete") || path.startsWith("/balance/employee")
					|| path.startsWith("/balance/shiftbalance")
					|| path.startsWith("/leave/employee") ||path.startsWith("/report")
					|| path.startsWith("/attendance/detailed-stats") || path.startsWith("/shifts/requestSwap")
					|| path.startsWith("/shifts/employee")||path.startsWith("/shifts/byEmployeeAndDate")||path.startsWith("/shifts/shiftByShiftId");
		}  
		return false;
	}

    private Mono<Void> handleUnauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
