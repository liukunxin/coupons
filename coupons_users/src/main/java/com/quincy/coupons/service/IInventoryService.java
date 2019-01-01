package com.quincy.coupons.service;

import com.quincy.coupons.vo.Response;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 获取库存信息: 只返回用户没有领取的
 * 优惠券库存功能实现接口定义
 */
public interface IInventoryService {
    /**
     * 获取库存信息
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    Response getInventoryInfo(Long userId) throws Exception;
}
