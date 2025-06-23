package com.shusl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 密码加密器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 注册 JdbcDaoImpl 作为 UserDetailsService
    @Bean
    public JdbcDaoImpl jdbcDao() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        jdbcDao.setUsersByUsernameQuery(
                "SELECT account AS username, password, true AS enabled FROM users WHERE account = ?");
        jdbcDao.setAuthoritiesByUsernameQuery(
                "SELECT account AS username, role FROM users WHERE account = ?");
        return jdbcDao;
    }

    // 认证提供者（使用 jdbcDao）
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jdbcDao());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 安全过滤器链配置
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/index", "/toLogin","/toRegister").permitAll()
                        .antMatchers("/level1/**").hasRole("VIP1")
                        .antMatchers("/level2/**").hasRole("VIP2")
                        .antMatchers("/level3/**").hasRole("VIP3")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/toLogin")
                        .loginProcessingUrl("/login")
                        .usernameParameter("user")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/toLogin?logout")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember")
                        .userDetailsService(jdbcDao()) // ⭐ 解决报错的关键
                );

        return http.build();
    }

    // 提供 AuthenticationManager（用于认证）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
