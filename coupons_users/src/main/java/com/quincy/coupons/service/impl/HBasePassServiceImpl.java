package com.quincy.coupons.service.impl;

import com.quincy.coupons.constant.Constants;
import com.quincy.coupons.service.IHBasePassService;
import com.quincy.coupons.utils.RowKeyGenerateUtil;
import com.quincy.coupons.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Quincy on 2018/12/31.
 * @Description Pass HBase 服务
 */
@Slf4j
@Service
public class HBasePassServiceImpl implements IHBasePassService {
    /**
     * HBase 客户端
     */
    private final HbaseTemplate hbaseTemplate;

    @Autowired
    public HBasePassServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean dropPassTemplateToHBase(PassTemplate passTemplate) {
        if (null == passTemplate)
            return false;
        String rowKey = RowKeyGenerateUtil.generatePassTemplateRowKey(passTemplate);
        try {
            if (hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME))
                    .exists(new Get(Bytes.toBytes(rowKey)))) {
                log.warn("RowKey is already exist!", rowKey);
                return false;
            }
        } catch (Exception e) {
            log.error("DropPassTemplateToHBase Error: {}", e.getMessage());
            return false;
        }
        Put put = new Put(Bytes.toBytes(rowKey));

        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.ID),
                Bytes.toBytes(passTemplate.getId())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.TITLE),
                Bytes.toBytes(passTemplate.getTitle())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.SUMMARY),
                Bytes.toBytes(passTemplate.getSummary())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.DESC),
                Bytes.toBytes(passTemplate.getDesc())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN),
                Bytes.toBytes(passTemplate.getHasToken())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND),
                Bytes.toBytes(passTemplate.getBackground())
        );

        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                Bytes.toBytes(passTemplate.getLimit())
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.START),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplate.getStart()))
        );
        put.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.END),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplate.getEnd()))
        );
        hbaseTemplate.saveOrUpdate(Constants.PassTemplateTable.TABLE_NAME, put);
        return true;
    }
}