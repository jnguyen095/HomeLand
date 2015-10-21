package com.test.session;

import com.test.domain.BrandEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface BrandLocalBean extends GenericSessionBean<BrandEntity, Long>{
}
