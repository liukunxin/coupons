package com.quincy.coupons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 用户领取优惠券的请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GainPassTemplateRequest {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 领取的优惠券对象
     */
    private PassTemplate passTemplate;
}
