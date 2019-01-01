package com.quincy.coupons.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 日志生成器
 */
@Slf4j
public class LogGenerator {
    /**
     * 生成Log
     * @param request {@link HttpServletRequest}
     * @param userId  用户id
     * @param action  日志类型
     * @param info    日志信息,可以是null
     */
    public static void generateLog(HttpServletRequest request, Long userId, String action, Object info) {
        log.info(
                JSON.toJSONString(
                        new LogObject(action, userId, System.currentTimeMillis(), request.getRemoteAddr(), info)
                )
        );
    }
}
