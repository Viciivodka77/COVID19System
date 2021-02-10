package graduatedesign.ghost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping(value = {"/","/home"})
    public String home(HttpServletRequest request){

        return "redirect:/dashboard";
    }
}
