package com.test.session.impl;

import com.test.domain.CrawlerHistoryEntity;
import com.test.session.CrawlerHistoryLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "CrawlerHistorySessionEJB")
public class CrawlerHistorySessionBean extends AbstractSessionBean<CrawlerHistoryEntity, Long> implements CrawlerHistoryLocalBean{
    @Override
    public CrawlerHistoryEntity findToday(Integer categoryId) {
        StringBuffer sql = new StringBuffer("FROM CrawlerHistoryEntity crw WHERE");
        sql.append(" crw.category.categoryId = :categoryId AND crw.updatedDate < :nextDate AND crw.updatedDate >= :today");
        Query query = entityManager.createQuery(sql.toString());
        Timestamp today = new Timestamp(System.currentTimeMillis());
        Timestamp nextDate = today;
        nextDate.setDate(today.getDate() + 1);

        query.setParameter("categoryId", categoryId);
        query.setParameter("today", today);
        query.setParameter("nextDate", nextDate);
        List<CrawlerHistoryEntity> object = query.getResultList();

        return object.size() > 0 ? object.get(0) : null;
    }
}
