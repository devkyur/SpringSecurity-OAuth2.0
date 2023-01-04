package kr.co.kyurworld.kyur.security.service;

import kr.co.kyurworld.kyur.member.memberMapper.MemberMapper;
import kr.co.kyurworld.kyur.security.mapper.UserMapper;
import kr.co.kyurworld.kyur.security.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserMapper userMapper;
    private final MemberMapper MemberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId; //임의 id
        String email = oAuth2UserInfo.getEmail();

        //String uuid = UUID.randomUUID().toString().substring(0, 6);
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String password = passwordEncoder.encode("패스워드"+uuid);

        UserVo uservo = userMapper.getUserInfo(username);

        //DB에 없는 사용자라면 회원가입
        if(uservo == null){
            UserVo uservo2 = new UserVo();
            uservo2.setUser_id(username);
            uservo2.setUser_password("");
            uservo2.setUser_name(oAuth2UserInfo.getName());
            uservo2.setUser_email(email);
            uservo2.setAttributes(oAuth2UserInfo.getAttributes());
            MemberMapper.insertOAuthMemberInfo(uservo2);
            uservo = uservo2;
        }

        return uservo;
    }
}
