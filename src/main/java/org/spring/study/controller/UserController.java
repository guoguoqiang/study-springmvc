package org.spring.study.controller;

import org.spring.study.model.User;
import org.spring.study.service.UserService;
import org.spring.study.springmvc.annotation.AutoWired;
import org.spring.study.springmvc.annotation.Controller;
import org.spring.study.springmvc.annotation.RequestMapping;
import org.spring.study.springmvc.annotation.ResponseBody;

/**
 * @Author ggq
 * @Date 2020/11/6 14:16
 */
@Controller
public class UserController {
    @AutoWired(value="userService")
    private UserService userService;


    //定义方法
    @RequestMapping("/findUser")
    public  String  findUser(){
        //调用服务层
        userService.findUser();
        return "forward:/success.jsp";
    }

    @RequestMapping("/getData")
    @ResponseBody  //返回json格式的数据
    public User getData(){
        //调用服务层
        return userService.getUser();
    }
}
