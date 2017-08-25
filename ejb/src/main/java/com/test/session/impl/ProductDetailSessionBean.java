package com.test.session.impl;

import com.test.domain.ProductDetailEntity;
import com.test.session.ProductDetailLocalBean;

import javax.ejb.Stateless;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/25/2017
 * Time: 5:24 PM
 */
@Stateless(name = "ProductDetailSessionEJB")
public class ProductDetailSessionBean extends AbstractSessionBean<ProductDetailEntity, Long> implements ProductDetailLocalBean{
}
