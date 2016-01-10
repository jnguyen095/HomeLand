package com.test.business;

import com.test.dto.UserGroupDTO;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface UserGroupManagementLocalBean {
    Object[] searchByProperties(Map<String,Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems);

    UserGroupDTO saveOrUpdate(UserGroupDTO pojo) throws DuplicateKeyException;

    UserGroupDTO findById(Integer userGroupId) throws ObjectNotFoundException;

    Boolean isDuplicated(String code, Integer id);

    List<UserGroupDTO> findAll();
}
