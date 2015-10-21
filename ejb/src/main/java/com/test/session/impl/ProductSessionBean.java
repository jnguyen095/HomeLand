package com.test.session.impl;

import com.test.domain.ProductEntity;
import com.test.session.ProductLocalBean;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "ProductSessionEJB")
public class ProductSessionBean extends AbstractSessionBean<ProductEntity, Long> implements ProductLocalBean{

    @Override
    public Boolean findByCode(String code) {
        StringBuffer stringBuffer = new StringBuffer("FROM ProductEntity prd WHERE prd.code = :code");
        Query query = entityManager.createQuery(stringBuffer.toString());
        query.setParameter("code", code);
        List object = query.getResultList();
        return object.size() > 0;
    }
}
