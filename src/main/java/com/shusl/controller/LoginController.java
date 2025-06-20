package com.shusl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // 跳转到登录页，可显示错误或注销提示
    @RequestMapping("/toLogin")
    public String toLogin(Model model,
                          @RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误，请重试！");
        }
        if (logout != null) {
            model.addAttribute("logout", "您已成功注销。");
        }
        return "views/login"; // 对应 login.html
    }
}
