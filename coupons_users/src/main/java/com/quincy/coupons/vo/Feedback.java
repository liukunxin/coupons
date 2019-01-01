package com.quincy.coupons.vo;

import com.google.common.base.Enums;
import com.quincy.coupons.constant.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Quincy on 2018/12/30.
 * 用户评论类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论类型
     */
    private String type;

    /**
     * PassTemplate RowKey ，如果是app类型的评论，则没有
     */
    private String templateId;

    /**
     * 评论内容
     */
    private String comment;

    public boolean validate(){
        //TODO 没理解透
        FeedbackType feedbackType = Enums.getIfPresent(
                FeedbackType.class,this.type.toUpperCase()
        ).orNull();

        return !(null == feedbackType || null == comment);
    }
}
