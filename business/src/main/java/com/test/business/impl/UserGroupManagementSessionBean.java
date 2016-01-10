package com.test.business.impl;

import com.test.business.UserGroupManagementLocalBean;
import com.test.domain.UserGroupEntity;
import com.test.dto.UserGroupDTO;
import com.test.session.UserGroupLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "UserGroupManagementSessionEJB")
public class UserGroupManagementSessionBean implements UserGroupManagementLocalBean{
    @EJB
    private UserGroupLocalBean userGroupLocalBean;

    @Override
    public Object[] searchByProperties(Map<String, Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems) {
        Object[] objs = userGroupLocalBean.searchByProperties(properties, sortExpression, sortDirection, firstItem, maxPageItems);
        List<UserGroupEntity> userGroupEntities = (List<UserGroupEntity>)objs[1];
        List<UserGroupDTO> userGroupDTOs = new ArrayList<>();
        for(UserGroupEntity userGroupEntity : userGroupEntities){
            userGroupDTOs.add(DozerSingletonMapper.getInstance().map(userGroupEntity, UserGroupDTO.class));
        }
        Object[] result = new Object[2];
        result[0] = objs[0];
        result[1] = userGroupDTOs;
        return result;
    }

    @Override
    public UserGroupDTO saveOrUpdate(UserGroupDTO pojo) throws DuplicateKeyException{
        UserGroupEntity entity = DozerSingletonMapper.getInstance().map(pojo, UserGroupEntity.class);
        Integer userGroupId = entity.getUserGroupId();
        if(userGroupId != null){
            entity = userGroupLocalBean.update(entity);
        }else{
            entity = userGroupLocalBean.save(entity);
        }
        return DozerSingletonMapper.getInstance().map(entity, UserGroupDTO.class);
    }

    @Override
    public UserGroupDTO findById(Integer userGroupId) throws ObjectNotFoundException{
        UserGroupEntity entity = userGroupLocalBean.findById(userGroupId);
        return DozerSingletonMapper.getInstance().map(entity, UserGroupDTO.class);
    }

    @Override
    public Boolean isDuplicated(String code, Integer id) {
        return userGroupLocalBean.isDuplicated(code, id);
    }

    @Override
    public List<UserGroupDTO> findAll() {
        List<UserGroupEntity> entities = userGroupLocalBean.findAll();
        List<UserGroupDTO> dtos = new ArrayList<>();
        for(UserGroupEntity entity : entities){
            dtos.add(DozerSingletonMapper.getInstance().map(entity, UserGroupDTO.class));
        }
        return dtos;
    }
}
