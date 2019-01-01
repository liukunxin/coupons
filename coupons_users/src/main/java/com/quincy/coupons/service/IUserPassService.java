package com.quincy.coupons.service;

import com.quincy.coupons.vo.Pass;
import com.quincy.coupons.vo.Response;

/**
 * Created by Quincy on 2018/12/31.
 * 获取用户个人优惠券信息接口
 */
public interface IUserPassService {
    /**
     * 获取用户个人优惠券信息(当前可以使用的优惠券),即"我的优惠券"功能实现
     *
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserPassInfo(Long userId) throws Exception;

    /**
     * 获取用户已经消费了的优惠券,即已使用优惠券功能实现
     *
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * 获取用户的所有优惠券
     *
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserAllPassInfo(Long userId) throws Exception;

    /**
     * 用户使用优惠券
     *
     * @param pass
     * @return {@link Response}
     */
    Response userUsedPass(Pass pass);
}
