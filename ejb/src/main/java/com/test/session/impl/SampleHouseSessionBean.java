package com.test.session.impl;

import com.test.domain.SampleHouseEntity;
import com.test.session.SampleHouseLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 7:59 AM
 */
@Stateless(name = "SampleHouseSessionEJB")
public class SampleHouseSessionBean extends AbstractSessionBean<SampleHouseEntity, Long> implements SampleHouseLocalBean {
    @Override
    public boolean alreadyCrawler(String url) {
        StringBuffer sql = new StringBuffer("FROM SampleHouseEntity sm WHERE sm.url = :url");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("url", url);
        return query.getResultList().size() > 0;
    }
}
