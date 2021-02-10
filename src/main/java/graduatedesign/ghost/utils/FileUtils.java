package graduatedesign.ghost.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUtils {


    public Map<String,String> saveImg(MultipartFile img) throws Exception {
        Map<String,String> map = new HashMap<>();
        //把图片存入本地 获得图片路径
        if (img.isEmpty()){
            throw new Exception("未上传图片");
        }
        String fileName = img.getOriginalFilename(); //文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); //后缀名
        fileName = UUID.randomUUID() + suffixName; //重命名
        String realPath = ResourceUtils.getURL("classpath:").getPath();
        realPath = realPath + "productImgs/" + fileName;
        File savedImg = new File(realPath);
        if (!savedImg.getParentFile().exists()){
            savedImg.getParentFile().mkdirs();
        }
        try {
            img.transferTo(savedImg);
        }catch (IOException e){
            e.printStackTrace();
        }

        map.put("realPath",realPath);
        map.put("fileName",fileName);
        return map;
    }

}
