package com.test.session.impl;

import com.test.domain.UnitEntity;
import com.test.session.UnitLocalBean;

import javax.ejb.Stateless;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/27/2017
 * Time: 11:58 AM
 */
@Stateless(name = "UnitSessionEJB")
public class UnitSessionBean extends AbstractSessionBean<UnitEntity, Integer> implements UnitLocalBean {
}
