package kr.co.kyurworld.kyur.security.service;

import kr.co.kyurworld.kyur.security.mapper.UserMapper;
import kr.co.kyurworld.kyur.security.model.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserVo loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserVo userVo = userMapper.getUserInfo(userId);

        if (userVo == null){
            throw new UsernameNotFoundException("User not authorized.");
        }

        return userVo;
    }
}
