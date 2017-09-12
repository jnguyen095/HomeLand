package com.test.session.impl;

import com.test.domain.NewsEntity;
import com.test.session.NewsLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;


/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 3:10 PM
 */
@Stateless
public class NewsSessionBean extends AbstractSessionBean<NewsEntity, Long> implements NewsLocalBean{
    @Override
    public boolean alreadyCrawler(String url) {
        StringBuffer sql = new StringBuffer("FROM NewsEntity n WHERE n.url = :url");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("url", url);
        return query.getResultList().size() > 0;
    }
}
