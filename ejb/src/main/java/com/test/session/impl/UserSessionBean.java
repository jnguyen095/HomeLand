package com.test.session.impl;

import com.test.domain.UserEntity;
import com.test.session.UserLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "UserSessionEJB")
public class UserSessionBean extends AbstractSessionBean<UserEntity, Integer> implements UserLocalBean {
    @Override
    public Boolean isDuplicated(String userName, Integer id) {
        StringBuffer sql = new StringBuffer("FROM UserEntity ug WHERE ug.userName = :userName");
        if(id != null){
            sql.append(" AND ug.userId <> :id");
        }
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("userName", userName);
        if(id != null){
            query.setParameter("id", id);
        }
        return query.getResultList().size() > 0;
    }
}
