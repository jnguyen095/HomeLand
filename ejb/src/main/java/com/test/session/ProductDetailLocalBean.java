package com.test.session;

import com.test.domain.ProductDetailEntity;

import javax.ejb.Local;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/25/2017
 * Time: 5:24 PM
 */
@Local
public interface ProductDetailLocalBean extends GenericSessionBean<ProductDetailEntity, Long>{
}
