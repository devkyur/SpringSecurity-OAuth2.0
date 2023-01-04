package kr.co.kyurworld.kyur.security.handler;

import kr.co.kyurworld.kyur.security.mapper.UserMapper;
import kr.co.kyurworld.kyur.security.model.UserVo;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    private final UserMapper userMapper;

    public LoginFailHandler(UserMapper userMapper) {
            this.userMapper = userMapper;
        }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String userId = request.getParameter("username");
        UserVo uservo = userMapper.getUserInfo(userId);

        HttpSession session = request.getSession();

        if (uservo == null) {
            session.setAttribute("msg", "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.");
        } else if (exception instanceof BadCredentialsException) {
            session.setAttribute("msg", "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.");
        } else if (exception instanceof InternalAuthenticationServiceException) {
            session.setAttribute("msg", "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            session.setAttribute("msg", "인증 요청이 거부되었습니다. 관리자에게 문의하세요.");
        } else {
            session.setAttribute("msg", "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/login");

    }
}