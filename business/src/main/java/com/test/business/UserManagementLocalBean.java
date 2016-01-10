package com.test.business;

import com.test.dto.UserDTO;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface UserManagementLocalBean {
    Object[] searchByProperties(Map<String,Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems);

    UserDTO saveOrUpdate(UserDTO pojo) throws DuplicateKeyException;

    UserDTO findById(Integer userGroupId) throws ObjectNotFoundException;

    Boolean isDuplicated(String userName, Integer id);
}
