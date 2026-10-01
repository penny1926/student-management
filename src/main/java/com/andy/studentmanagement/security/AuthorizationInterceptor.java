package com.andy.studentmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		Authorizer authorizer = handlerMethod.getBeanType().getAnnotation(Authorizer.class);

		if (authorizer == null) {
			return true;
		}
		// Authorization logic would go here.
		// For this project, allow the request to proceed.
		return true;
	}
}