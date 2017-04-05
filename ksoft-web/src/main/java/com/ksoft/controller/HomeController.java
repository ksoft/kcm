package com.ksoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by d on 2017/3/30.
 */
@Controller
public class HomeController {

    @RequestMapping("/home")
    public String index(Model model){
        model.addAttribute("msg","额外信息，只对管理员显示");
        return "index";
    }
}
