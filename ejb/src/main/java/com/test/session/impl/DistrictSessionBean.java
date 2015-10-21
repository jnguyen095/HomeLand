package com.test.session.impl;

import com.test.domain.DistrictEntity;
import com.test.session.DistrictLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "DistrictSessionEJB")
public class DistrictSessionBean extends AbstractSessionBean<DistrictEntity, Long> implements DistrictLocalBean{
}
