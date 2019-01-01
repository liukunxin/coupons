package com.quincy.coupons.service;

import com.quincy.coupons.vo.Feedback;
import com.quincy.coupons.vo.Response;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 评论功能接口
 */
public interface IFeedbackService {
    /**
     * 创建评论
     * @param feedback {@link Feedback}
     * @return {@link Response}
     */
    Response createFeedback(Feedback feedback);
    /**
     * 获取用户评论
     * @param userId 用户id
     * @return {@link Response}
     */
    Response getFeedback(Long userId);
}
