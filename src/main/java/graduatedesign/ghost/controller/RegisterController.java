package graduatedesign.ghost.controller;

import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.service.UserService;
import graduatedesign.ghost.utils.EncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private UserService userService;
    private EncoderUtils encoderUtils;

    @Autowired
    public RegisterController(UserService userService, EncoderUtils encoderUtils){
        this.userService = userService;
        this.encoderUtils = encoderUtils;
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String doRegister(@RequestParam("uName") String name,
                             @RequestParam("phone") Long phone,
                             @RequestParam("email") String email,
                             @RequestParam("uPassword") String password,
                             @RequestParam("ensurePwd") String ensurePwd,
                             @RequestParam("status") int status,
                             Model model){
        User checkedEmail = userService.selectUserByEmail(email);
        User checkedPhone = userService.selectUserByPhone(phone);
        if (checkedEmail != null){
            model.addAttribute("errorMsg","邮箱地址已经注册！");
            model.addAttribute("uName",name);
            model.addAttribute("email",email);
            model.addAttribute("phone",phone);
            return "register";
        }
        if (checkedPhone != null){
            model.addAttribute("errorMsg","手机号已经注册！");
            model.addAttribute("uName",name);
            model.addAttribute("email",email);
            model.addAttribute("phone",phone);
            return "register";
        }
        if (!ensurePwd.equals(password)){
            model.addAttribute("errorMsg","密码不一致！");
            model.addAttribute("uName",name);
            model.addAttribute("email",email);
            model.addAttribute("phone",phone);
            return "register";
        }else if (password.length() < 6 || password.length() > 25){
            model.addAttribute("errorMsg","密码长度异常！");
            model.addAttribute("uName",name);
            model.addAttribute("email",email);
            model.addAttribute("phone",phone);
            return "register";
        }
        String encodePassword = encoderUtils.passwordEncoder().encode(password);
        User registerUser = new User(name,phone,email.toLowerCase(),encodePassword,status);
        int i = userService.addUser(registerUser);
        //if (i == 0){
        //    //抛出异常
        //    model.addAttribute("errorMsg","注册失败!服务器出错,请稍后再试...");
        //    return "register";
        //}else {
        //}
        model.addAttribute("successMsg","注册成功！请登陆..");
        return "login";
    }
}
