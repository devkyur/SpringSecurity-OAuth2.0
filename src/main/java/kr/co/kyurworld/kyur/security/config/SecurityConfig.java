package kr.co.kyurworld.kyur.security.config;

import kr.co.kyurworld.kyur.security.handler.LoginAccessDeniedHandler;
import kr.co.kyurworld.kyur.security.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2Service OAuth2Service;
    private final AuthenticationSuccessHandler LoginSuccessHandler;
    private final AuthenticationFailureHandler LoginFailHandler;
    private final LogoutSuccessHandler LogoutSuccessHandler;
    private final LoginAccessDeniedHandler LoginAccessDeniedHandler;

    private static final String[] permitAllArray = {
        "/login"
        ,"/regi/**"
        ,"/find/**"
    };

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/css/**", "/js/**", "/img/**", "/lib/**", "/pdf/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(permitAllArray).permitAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            .and()
                .exceptionHandling().accessDeniedHandler(LoginAccessDeniedHandler)
            .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .successHandler(LoginSuccessHandler)
                .failureHandler(LoginFailHandler)
            .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessHandler(LogoutSuccessHandler)
            .and()
                .csrf().disable()
            .sessionManagement()
                .sessionFixation().changeSessionId()
                .invalidSessionUrl("/login")
            .and()
                .headers().frameOptions().sameOrigin()
            .and()
                // OAuth2기반 로그인
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .userInfoEndpoint()
                .userService(OAuth2Service);

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
