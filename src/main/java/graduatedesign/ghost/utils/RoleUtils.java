package graduatedesign.ghost.utils;

import graduatedesign.ghost.myEnum.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleUtils {

    //把数据库中的角色转换为字符
    //也可以直接在数据库中设置为vrchar格式

    public Role transformRoleToSecurity(Integer integer){
        if (integer == 1){
            return Role.ROLE_USER;
        }
        if (integer == 2){
            return Role.ROLE_ADMIN1;
        }
        if (integer == 3){
            return Role.ROLE_ADMIN2;
        }
        if (integer == 4){
            return Role.ROLE_ADMIN3;
        }
        return Role.ROLE_USELESS;
    }

    public String transformRoleToController(Integer integer) {
        if (integer == 1){
            return "普通用户";
        }
        if (integer == 2){
            return "一级管理员";
        }
        if (integer == 3){
            return "二级管理员";
        }
        if (integer == 4){
            return "三级管理员";
        }
        return "无效用户";
    }
}
