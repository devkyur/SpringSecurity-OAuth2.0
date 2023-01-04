package kr.co.kyurworld.kyur.main;

import kr.co.kyurworld.kyur.security.model.UserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {
    @RequestMapping("/")
    public ModelAndView main(Authentication authentication) throws Exception {
        ModelAndView mv = new ModelAndView("main/main");

        UserVo principal = (UserVo) authentication.getPrincipal();
        System.out.println(principal.getUser_id());

        return mv;
    }

    @RequestMapping("/admin/adminTestPage")
    public ModelAndView admin() {
        ModelAndView mv = new ModelAndView("admin/adminTestPage");

        return mv;
    }
}
