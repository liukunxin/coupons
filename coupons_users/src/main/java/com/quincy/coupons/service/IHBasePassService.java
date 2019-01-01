package com.quincy.coupons.service;

import com.quincy.coupons.vo.PassTemplate;

/**
 * Created by Quincy on 2018/12/31.
 * @Description Pass HBase 服务接口
 */
public interface IHBasePassService {
    /**
     * 将PassTemplate写入HBase
     * @param passTemplate {@link PassTemplate}
     * @return true/false
     */
    boolean dropPassTemplateToHBase(PassTemplate passTemplate);
}
