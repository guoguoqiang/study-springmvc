package org.spring.study.service.impl;

import org.spring.study.model.User;
import org.spring.study.service.UserService;
import org.spring.study.springmvc.annotation.Service;

/**
 * @Author ggq
 * @Date 2020/11/6 14:15
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Override
    public String findUser() {
        System.out.println("====调用UserServiceImpl==findUser===");
        return null;
    }

    @Override
    public User getUser() {

        return new User(1, "老王", "admin");
    }
}
