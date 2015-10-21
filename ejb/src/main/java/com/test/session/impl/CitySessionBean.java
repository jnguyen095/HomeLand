package com.test.session.impl;

import com.test.domain.CityEntity;
import com.test.session.CityLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "CitySessionEJB")
public class CitySessionBean extends AbstractSessionBean<CityEntity, Long> implements CityLocalBean{
}
