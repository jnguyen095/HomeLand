package com.test.session.impl;

import com.test.domain.ProductEntity;
import com.test.session.ProductLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "ProductSessionEJB")
public class ProductSessionBean extends AbstractSessionBean<ProductEntity, Long> implements ProductLocalBean{

    @Override
    public Boolean findByCode(String code) {
        StringBuffer stringBuffer = new StringBuffer("FROM ProductEntity prd WHERE prd.code = :code");
        Query query = entityManager.createQuery(stringBuffer.toString());
        query.setParameter("code", code);
        List object = query.getResultList();
        return object.size() > 0;
    }

    @Override
    public void updateProductWardDistrictAndCity(Integer wardId, Integer districtId, Integer cityId, Integer newWardId, Integer newDistrictId, Integer newCityId) {
        StringBuffer sql = new StringBuffer("update {h-schema}product set wardid = :wardid, districtid = :districtid, cityid = :cityid");
        sql.append(" where wardid = :owardid and districtid = :odistrictid and cityid = :ocityid");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("wardid", newWardId);
        query.setParameter("districtid", newDistrictId);
        query.setParameter("cityid", newCityId);

        query.setParameter("owardid", wardId);
        query.setParameter("odistrictid", wardId);
        query.setParameter("ocityid", wardId);
        query.executeUpdate();
    }

    @Override
    public void updateProductDistrictAndCity(Integer districtId, Integer cityId, Integer newDistrictId, Integer newCityId) {

    }

    @Override
    public void updateNewWard4Product(Integer wardId, Integer wardId1, Integer districtId) {
        StringBuffer sql = new StringBuffer("update {h-schema}product set wardid = :newId where wardid = :oddId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("newId", wardId1);
        query.setParameter("oddId", wardId);
        //query.setParameter("districtid", districtId);
        query.executeUpdate();
    }
}
