package com.test.session;

import com.test.domain.WardEntity;

import javax.ejb.Local;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface WardLocalBean extends GenericSessionBean<WardEntity, Long>{
    void updateNewDistrict(Integer districtId, Integer newDistrictId);

    List<WardEntity> findByDistrictId(Integer districtId);

    void deleteByListIds(List<Integer> deleteWardIds);
}
