package com.test.session.impl;

import com.test.domain.ProductAssetEntity;
import com.test.session.ProductAssetLocalBean;

import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "ProductAssetSessionEJB")
public class ProductAssetSessionBean extends AbstractSessionBean<ProductAssetEntity, Long> implements ProductAssetLocalBean{
}
