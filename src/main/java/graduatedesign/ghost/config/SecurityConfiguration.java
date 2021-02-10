package graduatedesign.ghost.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private PasswordEncoder passwordEncoder ;
    private UserDetailsService userDetailsService;


    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder,UserDetailsService userDetailsService){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /** 放行静态资源 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/imgs/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义登陆界面
        http.formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .failureForwardUrl("/login")
        .successForwardUrl("/login")
        .usernameParameter("userString")
        .passwordParameter("uPassword");

        http.logout().deleteCookies(new String[]{"status","uID"}).logoutSuccessUrl("/");

        http.rememberMe().userDetailsService(userDetailsService).tokenValiditySeconds(60 * 60 * 24 * 15);
        //解决非thymeleaf的form表单提交被拦截问题
        http.csrf().disable();


        http.authorizeRequests()
                //游客可浏览界面管理
                .antMatchers("/","/home","/login/email","/register","/login").permitAll()
                //普通用户可浏览界面管理
                .antMatchers("/dashboard","/dashboard/**","/product/**","/profile/**","/order/**","/ticket/**").hasRole("USER")
                //管理员可浏览界面管理
                .antMatchers("/admin/1/**").hasRole("ADMIN1")
                .antMatchers("/admin/2/**").hasRole("ADMIN2")
                .antMatchers("/admin/3/**").hasRole("ADMIN3")
                .anyRequest().authenticated();

    }



}
