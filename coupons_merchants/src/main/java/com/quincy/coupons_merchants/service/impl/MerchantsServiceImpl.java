package com.quincy.coupons_merchants.service.impl;

import com.alibaba.fastjson.JSON;
import com.quincy.coupons_merchants.constant.Constants;
import com.quincy.coupons_merchants.constant.ErrorCode;
import com.quincy.coupons_merchants.dao.MerchantsDao;
import com.quincy.coupons_merchants.entity.Merchants;
import com.quincy.coupons_merchants.service.IMerchantsService;
import com.quincy.coupons_merchants.vo.CreateMerchantsRequest;
import com.quincy.coupons_merchants.vo.CreateMerchantsResponse;
import com.quincy.coupons_merchants.vo.PassTemplate;
import com.quincy.coupons_merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Quincy on 2019/1/1.
 */
@Service
@Slf4j
public class MerchantsServiceImpl implements IMerchantsService {
    /**
     * 数据库接口
     */
    private final MerchantsDao merchantsDao;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MerchantsServiceImpl(MerchantsDao merchantsDao, KafkaTemplate<String, String> kafkaTemplate) {
        this.merchantsDao = merchantsDao;
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    @Transactional
    public Response createMerchants(CreateMerchantsRequest request) {
        Response response = new Response();
        CreateMerchantsResponse merchantsResponse = new CreateMerchantsResponse();
        ErrorCode errorCode = request.validate(merchantsDao);
        if (errorCode != ErrorCode.SUCCESS) {
            merchantsResponse.setId(-1);
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        } else {
            merchantsResponse.setId(merchantsDao.save(request.toMerchants()).getId());
        }
        response.setData(merchantsResponse);
        return response;
    }

    @Override
    public Response buildMerchantsInfoById(Integer id) {
        Response response = new Response();
        Optional<Merchants> optionalMerchants = merchantsDao.findById(id);
        if (!optionalMerchants.isPresent()) {
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXITS.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXITS.getDesc());
        } else {
            response.setData(optionalMerchants.get());
        }
        return response;
    }

    @Override
    public Response buildMerchantsInfoByName(String name) {
        Response response = new Response();
        Merchants merchants = merchantsDao.findByName(name);
        if (merchants == null) {
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXITS.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXITS.getDesc());
        } else {
            response.setData(merchants);
        }
        return response;
    }

    @Override
    public Response dropPassTemplate(PassTemplate template) {
        Response response = new Response();
        ErrorCode errorCode = template.validate(merchantsDao);
        if (errorCode != ErrorCode.SUCCESS) {
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        } else {
            String passTemplate = JSON.toJSONString(template);
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplate
            );
            log.info("DropPassTemplate: {" + passTemplate + "}");
        }
        return response;
    }
}
