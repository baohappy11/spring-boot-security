package com.shusl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //连接数据库
    @Autowired
    DataSource dataSource;
    //授权

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    //首页所有人可以访问

    http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/level1/**").hasRole("VIP1")
            .antMatchers("/level2/**").hasRole("VIP2")

            .antMatchers("/level3/**").hasRole("VIP3");

    //没有权限默认跳到登陆页面,需要开启登陆的界面
        http.formLogin().loginPage("/toLogin").usernameParameter("user").passwordParameter("pwd").loginProcessingUrl("/login");
    //退出
        http.logout().logoutSuccessUrl("/");

    //记住我,自定义前端接收的参数
        http.rememberMe().rememberMeParameter("remember");

    }
   // 认证
    //密码编码

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //User.UserBuilder users = User.withDefaultPasswordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP1","VIP2","VIP3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP1","VIP2")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP1");
    }
}















