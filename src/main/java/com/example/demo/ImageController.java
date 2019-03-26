package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

@Controller
@RequestMapping(value = "/image")
public class ImageController {

    private static final  String IMAGE_UPDATE_PATH = "E:\\workSpaces\\demo\\src\\main\\resources\\static\\images";

    private static final  String BASE_URL="/static/images/";

    @PostMapping(value = "/imgUpdate")
    @ResponseBody
    public String imgUpdate(@RequestParam("upfile") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return "error";
        }
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+path + BASE_URL;

        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + Math.random()*10 + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        File dest = new File(IMAGE_UPDATE_PATH +File.separator+ fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
            String config = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + basePath + fileName + "\"," +
                    "\"title\": \"" + fileName + "\"," +
                    "\"original\": \"" + fileName + "\"}";
            return config;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
