package com.quincy.coupons.service;

import com.quincy.coupons.vo.GainPassTemplateRequest;
import com.quincy.coupons.vo.Response;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 用户领取优惠券功能接口
 */
public interface IGainPassTemplateService {
    /**
     * 用户领取优惠券
     * @param request
     * @return
     * @throws Exception
     */
    Response gainPassTemplate(GainPassTemplateRequest request) throws Exception;

}
