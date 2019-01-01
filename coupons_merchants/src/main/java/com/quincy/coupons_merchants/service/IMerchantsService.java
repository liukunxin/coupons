package com.quincy.coupons_merchants.service;

import com.quincy.coupons_merchants.vo.CreateMerchantsRequest;
import com.quincy.coupons_merchants.vo.PassTemplate;
import com.quincy.coupons_merchants.vo.Response;

/**
 * Created by Quincy on 2019/1/1.
 * @Description 对商户服务接口定义
 */
public interface IMerchantsService {
    /**
     * 创建商户服务
     * @param request {@link CreateMerchantsRequest} 创建商户请求
     * @return {@link Response}
     */
    Response createMerchants(CreateMerchantsRequest request);

    /**
     * 根据id构建商户信息
     * @param id 商户id
     * @return {@link Response}
     */
    Response buildMerchantsInfoById(Integer id);

    /**
     * 根据name构建商户信息
     * @param name 商户名称
     * @return {@link Response}
     */
    Response buildMerchantsInfoByName(String name);

    /**
     * 投放优惠券
     * @param template {@link PassTemplate} 优惠券对象
     * @return {@link Response}
     */
    Response dropPassTemplate(PassTemplate template);
}
