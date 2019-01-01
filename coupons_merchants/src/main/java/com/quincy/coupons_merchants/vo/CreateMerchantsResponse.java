package com.quincy.coupons_merchants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Quincy on 2019/1/1.
 * 创建商户响应请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantsResponse {
    /**
     * 商户id： 创建失败则为-1
     */
    private Integer id;
}
