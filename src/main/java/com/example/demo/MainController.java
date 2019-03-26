package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model){
        return "index";
    }

    @RequestMapping(value = "/aa",method = RequestMethod.GET)
    public String aa(Model model){
        return "aa";
    }
}
