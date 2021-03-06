package com.quincy.coupons.mapper;

import com.quincy.coupons.constant.Constants;
import com.quincy.coupons.vo.Feedback;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by Quincy on 2018/12/30.
 */
public class FeedbackRowMapper implements RowMapper<Feedback>{
    private static byte[] FAMILY_I = Constants.FeedbackTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.FeedbackTable.USER_ID.getBytes();
    private static byte[] TYPE = Constants.FeedbackTable.TYPE.getBytes();
    private static byte[] TEMPLATE_ID = Constants.FeedbackTable.TEMPLATE_ID.getBytes();
    private static byte[] COMMENT = Constants.FeedbackTable.COMMENT.getBytes();

    @Override
    public Feedback mapRow(Result result, int i) throws Exception {
        Feedback feedback = new Feedback();

        feedback.setUserId(Bytes.toLong(result.getValue(FAMILY_I,USER_ID)));
        feedback.setType(Bytes.toString(result.getValue(FAMILY_I, TYPE)));
        feedback.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        feedback.setComment(Bytes.toString(result.getValue(FAMILY_I, COMMENT)));

        return feedback;
    }
}
