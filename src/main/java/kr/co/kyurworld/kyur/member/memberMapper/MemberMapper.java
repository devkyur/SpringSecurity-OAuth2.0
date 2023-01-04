package kr.co.kyurworld.kyur.member.memberMapper;

import kr.co.kyurworld.kyur.member.model.AuthNumInfo;
import kr.co.kyurworld.kyur.security.model.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void insertMemberInfo(UserVo uservo);
    void insertOAuthMemberInfo(UserVo uservo);
    int selectUserCnt(String userId);
    UserVo selectMember(String user_id);
    void insertAuthPwInfo(AuthNumInfo authNumInfo);
    int selectAuthPwInfo(String user_temp_pw);
    void updateUserPw(UserVo uservo);
}
