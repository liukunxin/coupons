package com.quincy.coupons_merchants.controller;

import com.alibaba.fastjson.JSON;
import com.quincy.coupons_merchants.service.IMerchantsService;
import com.quincy.coupons_merchants.vo.CreateMerchantsRequest;
import com.quincy.coupons_merchants.vo.PassTemplate;
import com.quincy.coupons_merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Quincy on 2019/1/1.
 */
@Slf4j
@RestController
@RequestMapping("/merchants")
public class MerchantsController {
    /**
     * 商户服务接口
     */
    private final IMerchantsService merchantsService;

    @Autowired
    public MerchantsController(IMerchantsService merchantsService) {
        this.merchantsService = merchantsService;
    }

    @ResponseBody
    @PostMapping("/create")
    public Response createMerchants(@RequestBody CreateMerchantsRequest request) {
        log.info("CreateMerchants: " + JSON.toJSONString(request));
        return merchantsService.createMerchants(request);
    }

    @ResponseBody
    @GetMapping("/id={id}")
    public Response buildMerchantsInfoById(@PathVariable Integer id) {
        log.info("buildMerchantsInfoById: " + id);
        return merchantsService.buildMerchantsInfoById(id);
    }

    @ResponseBody
    @GetMapping("/name={name}")
    public Response buildMerchantsInfoByName(@PathVariable String name) {
        log.info("buildMerchantsInfoByName: " + name);
        return merchantsService.buildMerchantsInfoByName(name);
    }

    @ResponseBody
    @PostMapping("/dropPassTemplate")
    public Response dropPassTemplate(@RequestBody PassTemplate passTemplate) {
        log.info("dropPassTemplate: " + JSON.toJSONString(passTemplate));
        return merchantsService.dropPassTemplate(passTemplate);
    }
}
