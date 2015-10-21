package com.test.session;

import com.test.domain.WardEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface WardLocalBean extends GenericSessionBean<WardEntity, Long>{
}
