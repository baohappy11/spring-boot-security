package com.shusl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 注入数据库连接池
    @Autowired
    DataSource dataSource;

    // 设置加密方式
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 权限控制（授权）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()//首页所有人可以访问
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");

        // 登录配置
        http.formLogin()
                .loginPage("/toLogin")          // 自定义登录页面
                .usernameParameter("user")      // 用户名参数
                .passwordParameter("pwd")       // 密码参数
                .loginProcessingUrl("/login"); // 登录提交接口


        // 登出
        http.logout()
        .logoutSuccessUrl("/toLogin?logout"); // 注销成功后跳转

        // 记住我功能
        http.rememberMe().rememberMeParameter("remember");
    }

    // 认证逻辑：数据库认证方式
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT account AS username, password, true AS enabled FROM users WHERE account = ?")
                .authoritiesByUsernameQuery("SELECT account AS username, role FROM users WHERE account = ?")
                .passwordEncoder(passwordEncoder());
    }
}
