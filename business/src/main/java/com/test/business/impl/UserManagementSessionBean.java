package com.test.business.impl;

import com.test.business.UserManagementLocalBean;
import com.test.domain.UserEntity;
import com.test.dto.UserDTO;
import com.test.session.UserLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "UserManagementSessionEJB")
public class UserManagementSessionBean implements UserManagementLocalBean {

    @EJB
    private UserLocalBean userLocalBean;

    @Override
    public Object[] searchByProperties(Map<String, Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems) {
        Object[] objs = userLocalBean.searchByProperties(properties, sortExpression, sortDirection, firstItem, maxPageItems);
        List<UserEntity> userEntities = (List<UserEntity>)objs[1];
        List<UserDTO> userDTOs = new ArrayList<>();
        for(UserEntity userEntity : userEntities){
            userDTOs.add(DozerSingletonMapper.getInstance().map(userEntity, UserDTO.class));
        }
        Object[] result = new Object[2];
        result[0] = objs[0];
        result[1] = userDTOs;
        return result;
    }

    @Override
    public UserDTO saveOrUpdate(UserDTO pojo) throws DuplicateKeyException {
        UserEntity entity = DozerSingletonMapper.getInstance().map(pojo, UserEntity.class);
        Integer userId = entity.getUserId();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(userId != null){
            entity.setUpdatedDate(now);
            entity = userLocalBean.update(entity);
        }else{
            entity.setCreatedDate(now);
            entity = userLocalBean.save(entity);
        }
        return DozerSingletonMapper.getInstance().map(entity, UserDTO.class);
    }

    @Override
    public UserDTO findById(Integer userId) throws ObjectNotFoundException {
        UserEntity entity = userLocalBean.findById(userId);
        return DozerSingletonMapper.getInstance().map(entity, UserDTO.class);
    }

    @Override
    public Boolean isDuplicated(String userName, Integer id) {
        return userLocalBean.isDuplicated(userName, id);
    }
}
