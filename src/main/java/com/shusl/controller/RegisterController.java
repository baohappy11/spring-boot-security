package com.shusl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // 访问注册页面
    @GetMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    // 提交注册请求
    @PostMapping("/register")
    public String doRegister(@RequestParam("account") String account,
                             @RequestParam("password") String password) {

        // 检查用户是否已存在
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user WHERE account = ?", Integer.class, account);

        if (count != null && count > 0) {
            // 用户已存在，返回注册页（这里简化处理，你也可以给出提示）
            return "redirect:/toRegister?error=exist";
        }

        // 存入数据库（明文密码）
        jdbcTemplate.update("INSERT INTO user(account, password) VALUES (?, ?)", account, password);

        // 注册成功后跳转到登录页面
        return "redirect:/toLogin";
    }
}
