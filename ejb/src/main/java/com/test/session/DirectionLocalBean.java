package com.test.session;

import com.test.domain.DirectionEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface DirectionLocalBean extends GenericSessionBean<DirectionEntity, Long>{
}
