package com.test.session.impl;

import com.test.domain.StreetEntity;
import com.test.session.StreetLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "StreetSessionEJB")
public class StreetSessionBean extends AbstractSessionBean<StreetEntity, Long> implements StreetLocalBean{
}
