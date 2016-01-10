package com.test.session;

import com.test.domain.UserGroupEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface UserGroupLocalBean extends GenericSessionBean<UserGroupEntity, Integer>{
    Boolean isDuplicated(String code, Integer id);
}
