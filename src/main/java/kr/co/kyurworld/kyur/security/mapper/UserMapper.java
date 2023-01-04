package kr.co.kyurworld.kyur.security.mapper;

import kr.co.kyurworld.kyur.security.model.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserVo getUserInfo(String userId);
}
