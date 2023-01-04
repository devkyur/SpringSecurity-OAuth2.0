package kr.co.kyurworld.kyur.security.handler;

import kr.co.kyurworld.kyur.security.model.UserVo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        HttpSession session = request.getSession();

        if (accessDeniedException instanceof AccessDeniedException) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !(((UserVo) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
                session.setAttribute("msg", "접근권한이 없는 사용자입니다.");
            }
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/err/deniedPage");
    }
}
