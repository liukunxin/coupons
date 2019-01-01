package com.quincy.coupons.mapper;

import com.quincy.coupons.constant.Constants;
import com.quincy.coupons.vo.User;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by Quincy on 2018/12/30.
 * @Description HBase User Row To User Object
 */
public class UserRowMapper implements RowMapper {
    private static byte[] FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
    private static byte[] NAME = Constants.UserTable.NAME.getBytes();
    private static byte[] AGE = Constants.UserTable.AGE.getBytes();
    private static byte[] SEX = Constants.UserTable.SEX.getBytes();

    private static byte[] FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
    private static byte[] PHONE = Constants.UserTable.PHONE.getBytes();
    private static byte[] ADDRESS = Constants.UserTable.ADDRESS.getBytes();
    @Override
    public Object mapRow(Result result, int i) throws Exception {
//        User user = new User();
//        user.setBaseInfo(new User.BaseInfo(Bytes.toString(result.getValue(FAMILY_B,NAME)),
//                Bytes.toInt(result.getValue(FAMILY_B,AGE)),
//                Bytes.toString(result.getValue(FAMILY_B,SEX))));
//        user.setOtherInfo(new User.OtherInfo(
//                Bytes.toString(result.getValue(FAMILY_O,PHONE)),
//                Bytes.toString(result.getValue(FAMILY_O,ADDRESS))
//        ));
//        user.setId(Bytes.toLong(result.getRow()));
        User.BaseInfo baseInfo = new User.BaseInfo(
                Bytes.toString(result.getValue(FAMILY_B,NAME)),
                Bytes.toInt(result.getValue(FAMILY_B,AGE)),
                Bytes.toString(result.getValue(FAMILY_B,SEX))
        );

        User.OtherInfo otherInfo = new User.OtherInfo(
                Bytes.toString(result.getValue(FAMILY_O,PHONE)),
                Bytes.toString(result.getValue(FAMILY_O,ADDRESS))
        );
        //result.getRow获得用户id，用户id也可以理解为RowKey
        return new User(Bytes.toLong(result.getRow()),baseInfo,otherInfo);
    }
}
