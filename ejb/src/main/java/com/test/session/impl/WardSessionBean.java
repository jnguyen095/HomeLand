package com.test.session.impl;

import com.test.domain.WardEntity;
import com.test.session.WardLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "WardSessionEJB")
public class WardSessionBean extends AbstractSessionBean<WardEntity, Long> implements WardLocalBean{
}
