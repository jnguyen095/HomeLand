package com.test.session;

import com.test.domain.DistrictEntity;

import javax.ejb.Local;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface DistrictLocalBean extends GenericSessionBean<DistrictEntity, Long>{
    List<DistrictEntity> findByCityId(long city1);

    DistrictEntity findByNameAndCityId(String district, Integer cityId);
}
