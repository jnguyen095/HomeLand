package com.test.business.impl;

import com.test.Constants;
import com.test.business.CategoryManagementLocalBean;
import com.test.business.CategoryManagementRemoteBean;
import com.test.domain.CategoryEntity;
import com.test.dto.CategoryDTO;
import com.test.dto.CategoryTreeDTO;
import com.test.session.CategoryLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "CategoryManagementSessionEJB")
public class CategoryManagementSessionBean implements CategoryManagementLocalBean, CategoryManagementRemoteBean{

    @EJB
    private CategoryLocalBean categoryLocalBean;

    @Override
    public void updateMainCategory(List<CategoryTreeDTO> trees) {
        try{
            for(CategoryTreeDTO tree : trees){
                CategoryDTO rootDTO = tree.getRoot();
                List<CategoryDTO> childDTO = tree.getNodes();
                CategoryEntity rootEntity = DozerSingletonMapper.getInstance().map(rootDTO, CategoryEntity.class);
                rootEntity.setActive(Constants.ACTIVE);
                rootEntity = categoryLocalBean.save(rootEntity);
                for(CategoryDTO child : childDTO){
                    CategoryEntity childEntity = DozerSingletonMapper.getInstance().map(child, CategoryEntity.class);
                    childEntity.setParent(rootEntity);
                    childEntity.setActive(Constants.ACTIVE);
                    categoryLocalBean.save(childEntity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
