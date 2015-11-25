package com.test.command;

import com.test.dto.ProductDTO;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 11/25/15
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCommand extends AbstractCommand<ProductDTO> {
    public ProductCommand(){
        this.pojo = new ProductDTO();
    }
}
