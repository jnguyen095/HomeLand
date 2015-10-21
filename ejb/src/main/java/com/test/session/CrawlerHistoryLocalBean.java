package com.test.session;

import com.test.domain.CrawlerHistoryEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface CrawlerHistoryLocalBean extends GenericSessionBean<CrawlerHistoryEntity, Long>{
    CrawlerHistoryEntity findToday(Integer categoryId);
}
