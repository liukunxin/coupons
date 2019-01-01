package com.quincy.coupons.dao;

import com.quincy.coupons.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Quincy on 2018/12/30.
 */
public interface MerchantsDao extends JpaRepository<Merchants, Integer> {
    /**
     * 通过商户id获取商户对象
     * @param id 商户id
     * @return {@link Merchants}
     */
    Optional<Merchants> findById(Integer id);

    /**
     * 通过商户名称获取商户对象
     * @param name 商户名称
     * @return {@link Merchants}
     */
    Merchants findByName(String name);

    /**
     * 通过商户id列表获取商户对象列表
     * @param ids 商户id列表
     * @return {@link Merchants}
     */
    List<Merchants> findByIdIn(List<Integer> ids);
}
