package com.ksoft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksoft.service.KcmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by d on 2017/3/30.
 */
@Configuration
@EnableWebSecurity
//用于@PreAuthorize的生效,基于方法的权限控制
@EnableGlobalMethodSecurity(prePostEnabled=true)
//覆盖默认的spring security提供的配置
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private KcmUserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //禁用CSRF保护
                .csrf().disable()
                .authorizeRequests()
                //任何访问都必须授权
                .anyRequest().fullyAuthenticated()
                //配置那些路径可以不用权限访问
                .mvcMatchers("/login").permitAll()
                .and()
                .formLogin()
                //登陆成功后的处理，因为是API的形式所以不用跳转页面
                .successHandler(new RestAuthenticationSuccessHandler())
                //登陆失败后的处理
                .failureHandler(new RestAuthenticationFailureHandler())
                .and()
                //登出后的处理
                .logout().logoutSuccessHandler(new RestLogoutSuccessHandler())
                .and()
                //认证不通过后的处理
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
        ;
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            logger.info(exception.getMessage());
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                String msg = exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException ?
                        "用户名或密码错误" : "验证码错误";
                ObjectMapper mapper = new ObjectMapper();
                out.write("登录失败");
                out.flush();
                out.close();
            } else {
                super.onAuthenticationFailure(request, response, exception);
            }
        }
    }


    /**
     * 登陆成功后的处理
     */
    public static class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                out.write("登录成功");
                logger.debug(authentication.getPrincipal());
                out.flush();
                out.close();
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }

    /**
     * 登出成功后的处理
     */
    public static class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request,
                                    HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {
            //Do nothing!
        }
    }

    /**
     * 权限不通过的处理
     */
    public static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication Failed: " + authException.getMessage());
        }
    }
}