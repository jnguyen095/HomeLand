package com.test.session.impl;

import com.test.domain.WardEntity;
import com.test.session.WardLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "WardSessionEJB")
public class WardSessionBean extends AbstractSessionBean<WardEntity, Long> implements WardLocalBean{
    @Override
    public void updateNewDistrict(Integer districtId, Integer newDistrictId) {
        StringBuffer sql = new StringBuffer("update {h-schema}ward set districtid = :newDistrictid");
        sql.append(" where districtid = :oddDistrictid");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("newDistrictid", newDistrictId);
        query.setParameter("oddDistrictid", districtId);
        query.executeUpdate();
    }

    @Override
    public List<WardEntity> findByDistrictId(Integer districtId) {
        StringBuffer sql = new StringBuffer("FROM WardEntity w where w.district.districtId = :districtId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("districtId", districtId);
        return query.getResultList();
    }

    @Override
    public void deleteByListIds(List<Integer> deleteWardIds) {
        StringBuffer sql = new StringBuffer("DELETE FROM WardEntity w where w.wardId in(:ids)");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("ids", deleteWardIds);
        query.executeUpdate();
    }
}
