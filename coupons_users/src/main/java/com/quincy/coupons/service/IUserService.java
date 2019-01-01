package com.quincy.coupons.service;

import com.quincy.coupons.vo.Response;
import com.quincy.coupons.vo.User;

/**
 * Created by Quincy on 2018/12/31.
 * 创建用户服务
 */
public interface IUserService {
    /**
     * 创建用户
     * @param user {@link User}
     * @return {@link Response}
     * @throws Exception
     */
    Response createUser(User user) throws Exception;

}
