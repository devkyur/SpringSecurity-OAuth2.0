package kr.co.kyurworld.kyur.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
@EqualsAndHashCode(of="username")
public class UserVo implements UserDetails, OAuth2User {
    private String user_id;
    private String user_password;
    private String user_name;
    private String user_email;
    private String user_role;
    private Map<String, Object> attributes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.user_role));
    }

    /**
     * 비밀번호를 리턴
     */
    @Override
    public String getPassword() {
        return this.user_password;
    }

    /**
     * 시큐리티의 userName -> 따라서 인증할 때 id를 봄
     */
    @Override
    public String getUsername() {
        return this.user_id;
    }

    /**
     * 계정 만료 여부
     * true : 만료안됨
     * false : 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정 비밀번호 만료 여부
     *  true : 만료 안됨
     *  false : 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     *  true : 활성화됨
     *  false : 활성화 안됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        String sub = getAttribute("sub").toString();
        return sub;
    }
}
