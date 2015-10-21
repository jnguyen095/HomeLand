package com.test.session.impl;

import com.test.domain.DirectionEntity;
import com.test.session.DirectionLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "DirectionSessionEJB")
public class DirectionSessionBean extends AbstractSessionBean<DirectionEntity, Long> implements DirectionLocalBean{
}
