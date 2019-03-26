package com.example.demo;

import com.baidu.ueditor.ActionEnter;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/static/jsp")
public class JSPController {
    @RequestMapping("/controller")
    @ResponseBody
    public String getConfigInfo(HttpServletRequest request, HttpServletResponse response) {
        String json = "";
                File jsonFile = null;
                try {
                    jsonFile = ResourceUtils.getFile("classpath:static/jsp/config_back.json");
                    json = FileUtils.readFileToString(jsonFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return json;
    }

}