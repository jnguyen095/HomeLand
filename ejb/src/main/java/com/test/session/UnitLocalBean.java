package com.test.session;

import com.test.domain.UnitEntity;

import javax.ejb.Local;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/27/2017
 * Time: 11:57 AM
 */
@Local
public interface UnitLocalBean extends GenericSessionBean<UnitEntity, Integer> {
}
