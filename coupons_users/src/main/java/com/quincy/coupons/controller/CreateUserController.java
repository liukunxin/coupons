package com.quincy.coupons.controller;

import com.quincy.coupons.log.LogConstants;
import com.quincy.coupons.log.LogGenerator;
import com.quincy.coupons.service.IUserService;
import com.quincy.coupons.vo.Response;
import com.quincy.coupons.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Quincy on 2018/12/31.
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class CreateUserController {
    /**
     * 创建用户服务
     */
    private final IUserService userService;
    /**
     * HttpServletRequest
     */
    private  HttpServletRequest httpServletRequest;

    @Autowired
    public CreateUserController(IUserService userService){
        this.userService = userService;
    }
    /**
     * 创建用户
     * @param user {@link User}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/createuser")
    Response createUser(@RequestBody User user) throws Exception {
        LogGenerator.generateLog(
                httpServletRequest,
                -1L,
                LogConstants.ActionName.CREATE_USER,
                user
        );
        return userService.createUser(user);
    }
}
