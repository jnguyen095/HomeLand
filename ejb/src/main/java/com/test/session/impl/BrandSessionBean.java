package com.test.session.impl;

import com.test.domain.BrandEntity;
import com.test.session.BrandLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "BrandSessionEJB")
public class BrandSessionBean extends AbstractSessionBean<BrandEntity, Long> implements BrandLocalBean{
}
