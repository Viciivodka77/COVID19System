package graduatedesign.ghost.utils;

import graduatedesign.ghost.myEnum.LogStatus;
import graduatedesign.ghost.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUtils {

    private int id = 0;

    private EncoderUtils encoderUtils;

    @Autowired
    public LoginUtils(EncoderUtils encoderUtils){
        this.encoderUtils = encoderUtils;
    }

    public int getId(){
        return id;
    }

    //判断登陆状态
    public boolean isLoginStatus(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String logStatus = null;
        int uID = 0;
        for (Cookie cookie : cookies) {
            if ("status".equals(cookie.getName())){
                logStatus = cookie.getValue();
            }
            if ("uID".equals(cookie.getName()) && !("").equals(cookie.getValue()) && cookie.getValue() != null){
                String encodeID = cookie.getValue();
                String result = encoderUtils.decodeAsciiToString(Base64Utils.decodeFromString(encodeID));
                try {
                    uID = Integer.parseInt(result);
                    id = uID;
                }catch (Exception e){
                    //log
                    e.printStackTrace();
                    uID = 0;
                    id = uID;
                }
            }
        }
        return uID != 0 && encoderUtils.passwordEncoder().matches(LogStatus.LOGIN_STATUS.getPwd(), logStatus);
    }

    public void loginSuccess(Model model, HttpServletResponse response, User loginUser) {
        model.addAttribute("uID",loginUser.getUID());
        byte[] id = (loginUser.getUID() + "Ghost").getBytes();
        String encodeID = Base64Utils.encodeToString(id);
        Cookie cookie = new Cookie("uID",encodeID);
        Cookie status = new Cookie("status",encoderUtils.passwordEncoder().encode("TRUE"));
        cookie.setMaxAge(60 * 60 * 24 * 15);
        status.setMaxAge(60 * 60 * 24 * 15);
        response.addCookie(cookie);
        response.addCookie(status);
    }
}
