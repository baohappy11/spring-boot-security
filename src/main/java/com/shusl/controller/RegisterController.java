package com.shusl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    // 跳转到注册页面
    @GetMapping("/toRegister")
    public String toRegister() {
        return "register"; // 对应 register.html
    }

    // 提交注册请求
    @PostMapping("/register")
    public String doRegister(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             Model model) {

        // 查询用户是否存在
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE account = ?", Integer.class, account);

        if (count != null && count > 0) {
            model.addAttribute("error", "该账号已存在，请更换账号！");
            return "register"; // 返回注册页面并展示错误
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(password);

        // 插入用户信息，默认角色为 ROLE_VIP0
        jdbcTemplate.update(
                "INSERT INTO users(account, password, role) VALUES (?, ?, ?)",
                account, encodedPassword, "ROLE_VIP0"
        );

        // 注册成功，重定向到登录页面
        return "redirect:/toLogin?account=" + account;

    }
}
