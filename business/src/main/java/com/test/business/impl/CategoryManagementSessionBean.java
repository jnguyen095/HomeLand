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
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                CategoryEntity rootEntity = null;
                try{
                    rootEntity = categoryLocalBean.findEqualUnique("name", rootDTO.getName());
                }catch (ObjectNotFoundException e){}

                if(rootEntity == null || rootEntity.getCategoryId() < 1){
                    rootEntity = DozerSingletonMapper.getInstance().map(rootDTO, CategoryEntity.class);
                    rootEntity.setActive(Constants.ACTIVE);
                    rootEntity = categoryLocalBean.save(rootEntity);
                }
                for(CategoryDTO child : childDTO){
                    CategoryEntity childEntity = null;
                    try{
                        childEntity = categoryLocalBean.findEqualUnique("name", child.getName());
                    }catch (ObjectNotFoundException e){}

                    if(childEntity == null || childEntity.getCategoryId() < 1){
                        childEntity = DozerSingletonMapper.getInstance().map(child, CategoryEntity.class);
                        childEntity.setParent(rootEntity);
                        childEntity.setActive(Constants.ACTIVE);
                        categoryLocalBean.save(childEntity);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object[] searchByProperties(Map<String, Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems) {
        Object[] objs = categoryLocalBean.searchByProperties(properties, sortExpression, sortDirection, firstItem, maxPageItems);
        List<CategoryEntity> listEntities = (List<CategoryEntity>)objs[1];
        List<CategoryDTO> listDTOs = new ArrayList<CategoryDTO>();
        for(CategoryEntity entity : listEntities){
            listDTOs.add(DozerSingletonMapper.getInstance().map(entity, CategoryDTO.class));
        }
        Object[] result = new Object[2];
        result[0] = objs[0];
        result[1] = listDTOs;
        return result;
    }
}
