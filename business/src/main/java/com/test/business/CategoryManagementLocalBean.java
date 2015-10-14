package com.test.business;

import com.test.dto.CategoryTreeDTO;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface CategoryManagementLocalBean {
    Object[] searchByProperties(Map<String,Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems);
}
