package com.test.session;

import com.test.domain.CityEntity;

import javax.ejb.Local;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface CityLocalBean extends GenericSessionBean<CityEntity, Long>{
    CityEntity findByName(String city);
}
