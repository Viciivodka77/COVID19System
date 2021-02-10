package graduatedesign.ghost.controller;

import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.pojo.ClockIn;
import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.service.ClockInService;
import graduatedesign.ghost.service.UserRoleService;
import graduatedesign.ghost.service.UserService;
import graduatedesign.ghost.utils.EncoderUtils;
import graduatedesign.ghost.utils.LoginUtils;
import graduatedesign.ghost.utils.RoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ProfileController {

    private UserService userService;
    private LoginUtils loginUtils;
    private EncoderUtils encoderUtils;
    private UserRoleService userRoleService;
    private RoleUtils roleUtils;
    private ClockInService clockInService;

    @Autowired
    public ProfileController(UserService userService,
                             LoginUtils loginUtils,
                             EncoderUtils encoderUtils,
                             UserRoleService userRoleService,
                             ClockInService clockInService,
                             RoleUtils roleUtils){
        this.userService = userService;
        this.loginUtils = loginUtils;
        this.encoderUtils = encoderUtils;
        this.userRoleService = userRoleService;
        this.clockInService = clockInService;
        this.roleUtils = roleUtils;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期 注意这里的转化要和传进来的字符串的格式一直 如2015-9-9 就应该为yyyy-MM-dd
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }


    @RequestMapping("/profile")
    public String toProfile(HttpServletRequest request,
                            Model model){
        //判断登陆状态
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            int id = loginUtils.getId();
            User user = userService.selectUserByUID(id);
            model.addAttribute("user",user);
            model.addAttribute("birthday",new Date(user.getBirthday()));
            List<Integer> roles = userRoleService.selectUserRoleByUid(user.getUID());
            Integer integer = roles.get(roles.size() - 1);
            String role = roleUtils.transformRoleToController(integer);
            model.addAttribute("role",role);
            return "profile";
        }else {
            //判断为未登录
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/profile/basic",method = RequestMethod.POST)
    public String alterBasic(@RequestParam("username") String uName,
                             @RequestParam("age") int age,
                             @RequestParam("birthday") Date birthday,
                             @RequestParam("address") String address,
                             @RequestParam("status") int status,
                             HttpServletRequest request){
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            int uID = loginUtils.getId();
            User updateUser = new User(uID,uName,age,birthday.getTime(),address,status);
            userService.updateUserBasic(updateUser);
            return "redirect:/profile";
        }else {
            //判断为未登录
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/profile/pwd",method = RequestMethod.GET)
    public String toPwd(HttpServletRequest request,
                        Model model){
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            return "profile_pwd";
        }else {
            //判断为未登录
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/profile/pwd",method = RequestMethod.POST)
    public String doPwd(@RequestParam("oldPwd") String oldPwd,
                        @RequestParam("newPwd") String newPwd,
                        @RequestParam("ensurePwd") String ensurePwd,
                        HttpServletRequest request,
                        Model model){
        boolean loginStatus = loginUtils.isLoginStatus(request);
        //验证登陆状态
        if (loginStatus){
            int id = loginUtils.getId();
            User oldUser = userService.selectUserByUID(id);
            //验证旧密码
            if (encoderUtils.passwordEncoder().matches(oldPwd,oldUser.getUPassword())){
                //验证密码长度
                if (!ensurePwd.equals(newPwd)){
                    model.addAttribute("errorMsg","密码不一致！");
                    return "profile_pwd";
                }else if (newPwd.length() < 6 || newPwd.length() > 25){
                    model.addAttribute("errorMsg","密码长度异常！");
                    return "profile_pwd";
                }
                //修改密码
                userService.updateUserPwd(id,newPwd);
                return "redirect:/profile";

            }else {
                model.addAttribute("errorMsg","旧密码错误");
                return "profile_pwd";
            }

        }else {
            //判断为未登录
            return "redirect:/login";
        }

    }


    @RequestMapping(value = "/profile/checkClockIn/{page}")
    public String toCheckClockIn(HttpServletRequest request,
                                 @PathVariable("page") String page,
                                 Model model){
        if (loginUtils.isLoginStatus(request)){
            PageInfo<ClockIn> clockInPageInfo = clockInService.querySomeoneClockIn(loginUtils.getId(), Integer.parseInt(page), 10);
            List<ClockIn> list = clockInPageInfo.getList();
            model.addAttribute("pageInfo",clockInPageInfo);
            model.addAttribute("urlString","/profile/checkClockIn/");
            return "profile_clock_in";
        }else{
            return "redirect:/login";
        }
    }

}
