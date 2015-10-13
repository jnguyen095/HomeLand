package com.test.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTreeDTO implements Serializable {

    private CategoryDTO root;
    private List<CategoryDTO> nodes;

    public List<CategoryDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<CategoryDTO> nodes) {
        this.nodes = nodes;
    }

    public CategoryDTO getRoot() {
        return root;
    }

    public void setRoot(CategoryDTO root) {
        this.root = root;
    }
}
