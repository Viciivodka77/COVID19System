package graduatedesign.ghost.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EncoderUtils {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public String decodeAsciiToString(byte[] encode){
        StringBuilder sb = new StringBuilder();
        String value = Arrays.toString(encode);
        value = value.substring(1,value.length()-1);
        //ascii转String
        String[] chars = value.split(", ");
        for (String aChar : chars) {
            sb.append((char) Integer.parseInt(aChar));
        }
        //检验
        int G = sb.indexOf("G");
        if (G == -1){
            //抛出异常 重新登陆
            return "0";
        }else {
            String salt = sb.substring(G);
            if ("Ghost".equals(salt)){
                return sb.substring(0, G);
            }else {
                //抛出异常 重新登陆
                return "0";
            }
        }

    }
}
