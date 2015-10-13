package com.test.session.impl;

import com.test.domain.CategoryEntity;
import com.test.session.CategoryLocalBean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class CategorySessionBean extends AbstractSessionBean<CategoryEntity, Long> implements CategoryLocalBean{

}
