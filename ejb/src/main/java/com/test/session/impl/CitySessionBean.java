package com.test.session.impl;

import com.test.domain.CityEntity;
import com.test.session.CityLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "CitySessionEJB")
public class CitySessionBean extends AbstractSessionBean<CityEntity, Long> implements CityLocalBean{
    @Override
    public CityEntity findByName(String city) {
        StringBuffer sql = new StringBuffer("FROM CityEntity ct where LOWER(ct.cityName) = :cityName");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("cityName", city.toLowerCase());
        return query.getResultList().size() > 0 ? (CityEntity) query.getResultList().get(0) : null;
    }
}
