package com.test.session.impl;

import com.test.Constants;
import com.test.domain.CategoryEntity;
import com.test.session.CategoryLocalBean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class CategorySessionBean extends AbstractSessionBean<CategoryEntity, Long> implements CategoryLocalBean{

    @Override
    public List<CategoryEntity> findLastCrawler() {
        StringBuffer sql = new StringBuffer("FROM CategoryEntity c WHERE c.crawled = :notCrawlerYet");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("notCrawlerYet", Constants.CRAWLER_NOT_YET);
        return query.getResultList();
    }

    @Override
    public void updateCrawlerStatus(int status) {
        StringBuffer sql = new StringBuffer("UPDATE CategoryEntity c SET c.crawled = :status");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("status", status);
        query.executeUpdate();
    }

    @Override
    public void updateCrawlerStatus4Category(Integer categoryId, int status) {
        StringBuffer sql = new StringBuffer("UPDATE CategoryEntity c SET c.crawled = :status");
        sql.append(" WHERE c.categoryId = :categoryId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("status", status);
        query.setParameter("categoryId", categoryId);
        query.executeUpdate();
    }
}
