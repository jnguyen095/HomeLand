package com.test.business;

import com.test.dto.CategoryDTO;
import com.test.dto.CategoryTreeDTO;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface CategoryManagementRemoteBean {
    void updateMainCategory(List<CategoryTreeDTO> trees);

    List<CategoryDTO> findAll(Boolean continueLastCrawler);

    void updateCrawlerStatus(Integer categoryId, int status);
}
