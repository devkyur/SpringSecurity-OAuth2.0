package kr.co.kyurworld.kyur.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) { // 1. 가려던 곳이 있는 경우
            new DefaultRedirectStrategy().sendRedirect(request, response, savedRequest.getRedirectUrl());
        } else { // 2. 가려던 곳이 없는 경우 (루트 페이지로 이동)
            new DefaultRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}