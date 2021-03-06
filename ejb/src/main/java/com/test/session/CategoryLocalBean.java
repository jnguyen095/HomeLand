package com.test.session;

import com.test.domain.CategoryEntity;

import javax.ejb.Local;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface CategoryLocalBean extends GenericSessionBean<CategoryEntity, Long> {
    List<CategoryEntity> findLastCrawler();

    void updateCrawlerStatus(int crawlerNotYet);

    void updateCrawlerStatus4Category(Integer categoryId, int status);
}
