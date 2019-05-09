package com.library.domain.config;

import com.library.service.impl.UserSecurityService;
import com.library.utility.SecurityUtility;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment env;

    @Autowired
    private UserSecurityService userSecurityService;

    private BCryptPasswordEncoder passwordEncoder()
    {
        return SecurityUtility.passwordEncoder();
    }

    private static final String [] PUBLIC_MATCHERS ={
            "css/**",
            "/js/**",
            "/",
            "/images/**",
            "/myAccount",
            "/newUser",
            "/forgetPassword",
            "/registerDetails",
            "/login",
            "/fonts/**",
            "/admin/adminHome",
            "/admin/addBook",
            "/admin/bookList",
            "/browse",
            "/aboutus",
            "/contactus",
            "/bookDetail",
            "/bookInfo",
            "/updateBook.html"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests().
                /*antMatchers("/**"). */
                antMatchers(PUBLIC_MATCHERS).
                permitAll();
//                anyRequest().
//                authenticated();
        http
                .csrf().disable().cors().disable()
                .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
                logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
                .and().
                rememberMe();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }
}
