package com.test.session.impl;

import com.test.domain.UserGroupEntity;
import com.test.session.UserGroupLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "UserGroupSessionEJB")
public class UserGroupSessionBean extends AbstractSessionBean<UserGroupEntity, Integer> implements UserGroupLocalBean {
    @Override
    public Boolean isDuplicated(String code, Integer id) {
        StringBuffer sql = new StringBuffer("FROM UserGroupEntity ug WHERE ug.code = :code");
        if(id != null){
            sql.append(" AND ug.userGroupId <> :id");
        }
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("code", code);
        if(id != null){
            query.setParameter("id", id);
        }
        return query.getResultList().size() > 0;
    }
}
