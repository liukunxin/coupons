package com.quincy.coupons.service.impl;

import com.alibaba.fastjson.JSON;
import com.quincy.coupons.constant.Constants;
import com.quincy.coupons.constant.PassStatus;
import com.quincy.coupons.dao.MerchantsDao;
import com.quincy.coupons.entity.Merchants;
import com.quincy.coupons.mapper.PassRowMapper;
import com.quincy.coupons.service.IUserPassService;
import com.quincy.coupons.vo.Pass;
import com.quincy.coupons.vo.PassInfo;
import com.quincy.coupons.vo.PassTemplate;
import com.quincy.coupons.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Quincy on 2018/12/31.
 * @Description 获取用户个人优惠券信息实现
 */
@Service
@Slf4j
public class UserPassServiceImpl implements IUserPassService{
    /**
     * Hbase 客户端
     */
    private final HbaseTemplate hbaseTemplate;

    /**
     * Merchants Dao
     */
    private final MerchantsDao merchantsDao;

    @Autowired
    public UserPassServiceImpl(HbaseTemplate hbaseTemplate, MerchantsDao merchantsDao) {
        this.hbaseTemplate = hbaseTemplate;
        this.merchantsDao = merchantsDao;
    }
    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId,PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId,PassStatus.ALL);
    }

    @Override
    public Response userUsedPass(Pass pass) {
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(
           String.valueOf(pass.getUserId())).reverse().toString());
        Scan scan = new Scan();
        List<Filter>filters = new ArrayList<>();
        filters.add(new PrefixFilter(rowPrefix));
        filters.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.TEMPLATE_ID.getBytes(),
                CompareOperator.EQUAL,
                Bytes.toBytes(pass.getTemplateId())
        ));
        filters.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.CONSUMER_DATE.getBytes(),
                CompareOperator.EQUAL,
                Bytes.toBytes("-1")
        ));
        scan.setFilter(new FilterList(filters));
        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME,scan,new PassRowMapper());
        if (passes == null || passes.size()!=1){
            log.error("UserUsePass Error: {}", JSON.toJSONString(pass));
            return Response.failure("UserUsePass Error");
        }
        byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] CONSUMER_DATE = Constants.PassTable.CONSUMER_DATE.getBytes();
        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(FAMILY_I,CONSUMER_DATE,Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        datas.add(put);
        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME,datas);
        return Response.success();
    }
    /**
     * 根据优惠券状态获取优惠券信息
     *
     * @param userId 用户id
     * @param status {@link PassStatus}
     * @return {@link Response}
     */
    private Response getPassInfoByStatus(Long userId, PassStatus status) throws Exception{
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());
        CompareOperator compareOp = status == PassStatus.UNUSED ?
                CompareOperator.EQUAL : CompareOperator.NOT_EQUAL;
        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        // 1.行键前缀过滤器,找到特定的用户的优惠券
        filters.add(new PrefixFilter(rowPrefix));
        // 2.基于列单元值的过滤器,找到未使用的优惠券
        if (status != PassStatus.ALL){
            filters.add(
                    new SingleColumnValueFilter(
                            Constants.PassTable.FAMILY_I.getBytes(),
                            Constants.PassTable.CONSUMER_DATE.getBytes(),
                            compareOp,
                            Bytes.toBytes("-1")
                    )
            );
        }
        scan.setFilter(new FilterList(filters));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(new ArrayList<>(passTemplateMap.values()));

        List<PassInfo> result = new ArrayList<>();
        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);
            if (passTemplate == null) {
                log.error("PassTemplate Null: {}" + pass.getTemplateId());
                continue;
            }
            Merchants merchants = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getId());
                continue;
            }
            result.add(new PassInfo(pass, passTemplate, merchants));
        }
        return new Response(result);
    }

    /**
     * 通过获取的Passes对象构造Map
     * @param passes {@link Pass}
     * @return {@link PassTemplate}
     * @throws Exception
     */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws Exception {
        String[] patterns = new String[]{"yyyy-MM-dd"};
        byte[] FAMILY_B = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(Constants.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(Constants.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(Constants.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(Constants.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(Constants.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(Constants.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(Constants.PassTemplateTable.END);

        List<String> templateIds = passes.stream().map(
                Pass::getTemplateId
        ).collect(Collectors.toList());
        List<Get> templateGets = new ArrayList<>();
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));
        Result[] templateResults = hbaseTemplate.getConnection()
                .getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME))
                .get(templateGets);
        // 构造 PassTemplateId -> PassTemplate Object 的 Map, 用于构造PassInfo
        Map<String, PassTemplate> template2Object = new HashMap<>();
        for (Result item : templateResults) {
            PassTemplate passTemplate = new PassTemplate();

            passTemplate.setId(Bytes.toInt(item.getValue(FAMILY_B, ID)));
            passTemplate.setTitle(Bytes.toString(item.getValue(FAMILY_B, TITLE)));
            passTemplate.setSummary(Bytes.toString(item.getValue(FAMILY_B, SUMMARY)));
            passTemplate.setDesc(Bytes.toString(item.getValue(FAMILY_B, DESC)));
            passTemplate.setHasToken(Bytes.toBoolean(item.getValue(FAMILY_B, HAS_TOKEN)));
            passTemplate.setBackground(Bytes.toInt(item.getValue(FAMILY_B, BACKGROUND)));

            passTemplate.setLimit(Bytes.toLong(item.getValue(FAMILY_C, LIMIT)));
            passTemplate.setStart(DateUtils.parseDate(
                    Bytes.toString(item.getValue(FAMILY_C, START)), patterns));
            passTemplate.setEnd(DateUtils.parseDate(
                    Bytes.toString(item.getValue(FAMILY_C, END)), patterns
            ));
            template2Object.put(Bytes.toString(item.getRow()), passTemplate);
        }
        return template2Object;
    }
    /**
     * 通过获取的 PassTemplate 对象构造 Merchants Map
     *
     * @param passTemplates {@link PassTemplate}
     * @return {@link Merchants}
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate> passTemplates) {
        Map<Integer, Merchants> merchantsMap = new HashMap<>();

        List<Integer> merchantsIds = passTemplates.stream().map(
                PassTemplate::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIdIn(merchantsIds);

        merchants.forEach(m -> merchantsMap.put(m.getId(), m));

        return merchantsMap;
    }
}
