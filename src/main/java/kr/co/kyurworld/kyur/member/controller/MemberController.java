package kr.co.kyurworld.kyur.member.controller;

import kr.co.kyurworld.kyur.member.model.AuthNumInfo;
import kr.co.kyurworld.kyur.member.service.MemberService;
import kr.co.kyurworld.kyur.security.model.UserVo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

@Controller
public class MemberController {
    private final MemberService memberService;
    private final JavaMailSender javaMailSender;

    public MemberController(MemberService memberService, JavaMailSender javaMailSender) {
        this.memberService = memberService;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session){
        if (session.getAttribute("msg") != null) {
            request.setAttribute("msg", session.getAttribute("msg"));
            session.removeAttribute("msg");
        }

        return "login/login";
    }

    @RequestMapping("/regi/registerPage")
    public ModelAndView registerPage() {
        ModelAndView mv = new ModelAndView("login/registerPage");

        return mv;
    }

    @GetMapping(value = "/err/deniedPage")
    public String accessDenied(HttpServletRequest request, HttpSession session){
        if (session.getAttribute("msg") != null) {
            request.setAttribute("msg", session.getAttribute("msg"));
            session.removeAttribute("msg");
        }

        return "err/deniedPage";
    }

    @RequestMapping("/regi/register")
    public String register(@ModelAttribute UserVo uservo) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode(uservo.getUser_password());
        uservo.setUser_password(pw);

        memberService.insertMemberInfo(uservo);

        return "redirect:/login";
    }

    @ResponseBody
    @RequestMapping("/regi/checkId")
    public int idCheck(HttpServletRequest request) {
        String userId = request.getParameter("id");
        int checkIdCnt = memberService.checkId(userId);
        return checkIdCnt;
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println(attributes);

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();

       return attributes.toString();
    }

    @RequestMapping("/find/findPassword")
    public ModelAndView findPassword() {
        ModelAndView mv = new ModelAndView("login/find/findPassword");
        return mv;
    }

    @RequestMapping("/find/sendEmail")
    @ResponseBody
    public String sendEmail(HttpServletRequest request, HttpSession session) {
        String result = "F";

        String user_id = request.getParameter("user_id");
        UserVo uservo = memberService.selectMember(user_id);

        if (uservo != null) {
            Random r = new Random();
            int num = r.nextInt(999999); // 랜덤난수설정

            String content = System.lineSeparator() + "안녕하세요 회원님" + System.lineSeparator()
                            + "비밀번호찾기(변경) 인증번호는 " + num + " 입니다." + System.lineSeparator();

            // 대상 발송이기에 사용X
            //ArrayList<String> userList = new ArrayList<>();
            //userList.add("dlarbgur11@naver.com");
            //userList.add("rbgur1122@gmail.com");
            //message.setTo((String[]) userList.toArray(new String[userList.size()]));

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(uservo.getUser_email());
                message.setFrom("rbgur1122@gmail.com");
                message.setSubject("[Kyur World] 임시 비밀번호 안내");
                message.setText(content);
                javaMailSender.send(message);
                
                // 비밀번호 찾기 인증번호 테이블 저장
                AuthNumInfo authNumInfo = new AuthNumInfo();
                authNumInfo.setUser_id(user_id);
                authNumInfo.setUser_email(uservo.getUser_email());
                authNumInfo.setAuth_num(num);
                authNumInfo.setUser_name(uservo.getUser_name());

                memberService.insertAuthPwInfo(authNumInfo);

                session.setAttribute("user_id", user_id);
                result = "S";

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping("/find/inputTempPw")
    public ModelAndView inputTempPw() {
        ModelAndView mv = new ModelAndView("login/find/inputTempPw");
        return mv;
    }

    @RequestMapping("/find/checkTempPw")
    @ResponseBody
    public int checkTempPw(HttpServletRequest request) {
        String user_temp_pw = request.getParameter("user_temp_pw");

        int cnt = memberService.checkTempPw(user_temp_pw);

        return cnt;
    }

    @RequestMapping("/find/inputNewPw")
    public ModelAndView inputNewPw() {
        ModelAndView mv = new ModelAndView("login/find/inputNewPw");
        return mv;
    }

    @RequestMapping("/find/setNewPw")
    @ResponseBody
    public String setNewPw(@ModelAttribute UserVo uservo, HttpSession session){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode(uservo.getUser_password());
        uservo.setUser_password(pw);

        if (session.getAttribute("user_id") != null) {
            uservo.setUser_id(session.getAttribute("user_id").toString());
            session.removeAttribute("user_id");
        }

        memberService.updateUserPw(uservo);

        return "S";
    }
}
