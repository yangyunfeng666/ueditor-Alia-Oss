package com.example.demo;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/image")
public class ImageController {

    private static final  String IMAGE_UPDATE_PATH = "E:\\workSpaces\\demo\\src\\main\\resources\\static\\images";

    private static final  String BASE_URL= "https://minority.oss-cn-shanghai.aliyuncs.com/";

    private static final String IMAGE_ACTION = "?x-oss-process=image/resize,m_lfit,h_512,w_512";
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    String accessKeyId = "LTAI5SoPU0C3aECR";
    String accessKeySecret = "DOcT0WrbdMzsqB6HWQbmr6tsOh53l3";
    String bucketName = "minority";

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
            ossClient.putObject("minority", "article/img_"+fileName, inputStream);

// 关闭OSSClient。
            ossClient.shutdown();

          //  file.transferTo(dest);
            //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
            String config = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + BASE_URL + "article/img_"+fileName + IMAGE_ACTION+"\"," +//默认的oss图片处理
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

    @RequestMapping(value = "/listImage",method = RequestMethod.GET)
    @ResponseBody
    private String listImage(@RequestParam(value = "page",defaultValue = "1",required = false) int page,@RequestParam(value = "pageSize",required = false,defaultValue = "100") int pageSize){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        final int maxKeys = 200;
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix("article/img_").withMaxKeys(maxKeys));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            System.out.println("\t" + s.getKey());
        }
        ossClient.shutdown();
        if( !sums.isEmpty() ){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state","SUCCESS");
            JSONArray jsonArray = new JSONArray();
            for (OSSObjectSummary summary:sums){
                JSONObject json = new JSONObject();
                json.put("url",BASE_URL+summary.getKey()+IMAGE_ACTION);
                jsonArray.put(json);
            }
            jsonObject.put("list",jsonArray);
            jsonObject.put("total",sums.size());
            jsonObject.put("start",(page-1)*pageSize);
            System.out.println(jsonObject.toString());
            return jsonObject.toString();
        }else{
            return "";
        }
    }
}
