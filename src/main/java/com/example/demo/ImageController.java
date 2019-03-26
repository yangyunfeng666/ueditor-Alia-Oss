package com.example.demo;

import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;

@Controller
@RequestMapping(value = "/image")
public class ImageController {

    private static final  String IMAGE_UPDATE_PATH = "E:\\workSpaces\\demo\\src\\main\\resources\\static\\images";

    private static final  String BASE_URL= "https://minority.oss-cn-shanghai.aliyuncs.com/article/";

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    String accessKeyId = "LTAI5SoPU0C3aECR";
    String accessKeySecret = "DOcT0WrbdMzsqB6HWQbmr6tsOh53l3";


    @PostMapping(value = "/imgUpdate")
    @ResponseBody
    public String imgUpdate(@RequestParam("upfile") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return "error";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + Math.random()*10 + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        try {
// 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
            InputStream inputStream = file.getInputStream();
            ossClient.putObject("minority", "article/"+fileName, inputStream);

// 关闭OSSClient。
            ossClient.shutdown();

          //  file.transferTo(dest);
            //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
            String config = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + BASE_URL + fileName + "\"," +
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
