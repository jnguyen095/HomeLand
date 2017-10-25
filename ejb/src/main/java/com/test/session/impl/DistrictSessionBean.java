package com.test.session.impl;

import com.test.domain.DistrictEntity;
import com.test.session.DistrictLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "DistrictSessionEJB")
public class DistrictSessionBean extends AbstractSessionBean<DistrictEntity, Long> implements DistrictLocalBean{
    @Override
    public List<DistrictEntity> findByCityId(long cityId) {
        StringBuffer sql = new StringBuffer("FROM DistrictEntity dt WHERE dt.city.cityId = :CityID");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("CityID", cityId);
        return query.getResultList();
    }

    @Override
    public DistrictEntity findByNameAndCityId(String district, Integer cityId) {
        StringBuffer sql = new StringBuffer("FROM DistrictEntity dt WHERE dt.city.cityId = :CityID and LOWER(dt.districtName) = :districtName");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("CityID", cityId);
        query.setParameter("districtName", district.toLowerCase());
        return query.getResultList().size() > 0 ? (DistrictEntity) query.getResultList().get(0) : null;
    }
}
