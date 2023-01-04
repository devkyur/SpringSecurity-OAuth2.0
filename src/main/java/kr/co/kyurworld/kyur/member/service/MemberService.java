package kr.co.kyurworld.kyur.member.service;

import kr.co.kyurworld.kyur.member.memberMapper.MemberMapper;
import kr.co.kyurworld.kyur.member.model.AuthNumInfo;
import kr.co.kyurworld.kyur.security.model.UserVo;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberMapper MemberMapper;

    public MemberService(MemberMapper memberMapper) {
        MemberMapper = memberMapper;
    }

    public void insertMemberInfo(UserVo uservo) {
        try {
            MemberMapper.insertMemberInfo(uservo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkId(String userId) {
        int checkUserCnt = 0;

        try {
            checkUserCnt = MemberMapper.selectUserCnt(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return checkUserCnt;
    }

    public UserVo selectMember(String user_id) {
        UserVo uservo = null;
        try {
            uservo = MemberMapper.selectMember(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uservo;
    }

    public void insertAuthPwInfo(AuthNumInfo authNumInfo) {
        try {
            MemberMapper.insertAuthPwInfo(authNumInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkTempPw(String user_temp_pw) {
        int result = 0;

        try {
            result = MemberMapper.selectAuthPwInfo(user_temp_pw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updateUserPw(UserVo uservo) {
        try {
            MemberMapper.updateUserPw(uservo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
