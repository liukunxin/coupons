package com.quincy.coupons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 库存请求响应VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 优惠券模板信息
     */
    private List<PassTemplateInfo> passTemplateInfos;
}
