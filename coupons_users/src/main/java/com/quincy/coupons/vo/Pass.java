package com.quincy.coupons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Quincy on 2018/12/30.
 * 用户领取的优惠券
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pass {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * pass 在 HBase中的RowKey
     */
    private String rowKey;

    /**
     * PassTemplate在HBase中的RowKey
     */
    private String templateId;

    /**
     * 优惠券token，有可能是null，则填充-1
     */
    private String token;

    /**
     * 领取日期
     */
    private Date assignDate;

    /**
     * 消费日期，不为空代表已经被消费
     */
    private Date consumeDate;
}
