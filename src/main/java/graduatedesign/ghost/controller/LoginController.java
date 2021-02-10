package graduatedesign.ghost.controller;

import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.service.UserService;
import graduatedesign.ghost.utils.EncoderUtils;
import graduatedesign.ghost.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private UserService userService;
    private EncoderUtils encoderUtils;
    private LoginUtils loginUtils;

    @Autowired
    public LoginController(UserService userService, EncoderUtils encoderUtils,LoginUtils loginUtils){
        this.userService = userService;
        this.encoderUtils = encoderUtils;
        this.loginUtils = loginUtils;
    }

    @RequestMapping(value = {"/login"},method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request){
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            return "redirect:/dashboard";
        }else {
            return "login";
        }
    }
    @RequestMapping(value = {"/login/email"},method = RequestMethod.GET)
    public String toLoginByEmail(HttpServletRequest request){
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            return "redirect:/dashboard";
        }else {
            return "login_email";
        }
    }
    @RequestMapping(value = {"/login"},method = RequestMethod.POST)
    public String doLogin(@RequestParam("userString") String userString,
                          @RequestParam("uPassword") String uPassword,
                          Model model,
                          HttpServletResponse response){

        //判断手机号码登陆 or 邮箱地址登陆
        if ( !userString.contains("@")){
            //业务
            User loginUser = userService.selectUserByPhone(Long.parseLong(userString));
            if (loginUser != null){
                if (encoderUtils.passwordEncoder().matches(uPassword,loginUser.getUPassword())){
                    //成功
                    loginUtils.loginSuccess(model,response,loginUser);
                    return "redirect:dashboard";
                }else {
                    //失败
                    model.addAttribute("errorMsg","密码错误");
                    return "login";
                }
            }else {
                //失败
                model.addAttribute("errorMsg","用户名不存在");
                return "login";
            }
        }else {
            User loginUser = userService.selectUserByEmail(userString);
            if (loginUser != null){
                if (encoderUtils.passwordEncoder().matches(uPassword,loginUser.getUPassword())){
                    //成功
                    loginUtils.loginSuccess(model,response,loginUser);
                    return "redirect:dashboard";
                }else {
                    //失败
                    model.addAttribute("errorMsg","密码错误");
                    return "login_email";
                }
            }else {
                //失败
                model.addAttribute("errorMsg","邮箱不存在");
                return "login_email";
            }
        }

    }

}
