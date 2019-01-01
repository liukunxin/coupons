package com.quincy.coupons.vo;

import com.quincy.coupons.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 用户领取的优惠券信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfo {
    /**
     * 优惠券
     */
    private Pass pass;

    /**
     * 优惠券模板
     */
    private PassTemplate passTemplate;

    /**
     * 优惠券商户对象
     */
    private Merchants merchants;
}
