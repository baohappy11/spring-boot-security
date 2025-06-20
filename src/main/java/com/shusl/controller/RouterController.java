package com.shusl.controller;

import org.springframework.stereotype.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouterController {

    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }

//    @RequestMapping("/toLogin")
//    public String toLogin(){
//        return "views/login";
//    }
    //注销
//    @RequestMapping("/logout")
//    public String logout(){
//        return "index";
//        }

    //访问注册页面
    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id){

        return "views/level1/"+id;
    }

    @RequestMapping("/level2/{id}")
    public String level2(@PathVariable("id") int id){

        return "views/level2/"+id;
    }

    @RequestMapping("/level3/{id}")
    public String level3(@PathVariable("id") int id){

        return "views/level3/"+id;
    }

}
