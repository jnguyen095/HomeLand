package com.test.business;

import javax.ejb.Local;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 11/25/15
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface ProductManagementLocalBean {
    Object[] searchByProperties(Map<String,Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems);
}
