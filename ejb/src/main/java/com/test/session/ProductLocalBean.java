package com.test.session;

import com.test.domain.ProductEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface ProductLocalBean extends GenericSessionBean<ProductEntity, Long>{
    Boolean findByCode(String code);

    void updateProductWardDistrictAndCity(Integer wardId, Integer districtId, Integer cityId, Integer wardId1, Integer districtId1, Integer cityId1);

    void updateProductDistrictAndCity(Integer districtId, Integer cityId, Integer districtId1, Integer cityId1);

    void updateNewWard4Product(Integer wardId, Integer wardId1, Integer districtId);
}
