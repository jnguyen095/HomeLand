package com.test.command;

import com.test.dto.CategoryDTO;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/22/15
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryCommand extends AbstractCommand<CategoryDTO> {

    public CategoryCommand(){
        this.pojo = new CategoryDTO();
    }
}
