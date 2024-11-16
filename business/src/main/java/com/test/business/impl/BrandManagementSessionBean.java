package com.test.business.impl;

import com.test.business.BrandManagementLocalBean;
import com.test.business.BrandManagementRemoteBean;
import com.test.domain.BrandEntity;
import com.test.dto.BrandDTO;
import com.test.session.BrandLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/22/2017
 * Time: 5:58 PM
 */
@Stateless(name = "BrandManagementSessionEJB")
public class BrandManagementSessionBean implements BrandManagementRemoteBean, BrandManagementLocalBean {
    @EJB
    private BrandLocalBean brandLocalBean;

    @Override
    public List<BrandDTO> findAll() {
        List<BrandEntity> brandEntities = brandLocalBean.findAll();
        List<BrandDTO> brandDTOs = new ArrayList<>();
        for(BrandEntity brandEntity : brandEntities){
            brandDTOs.add(DozerSingletonMapper.getInstance().map(brandEntity, BrandDTO.class));
        }
        return brandDTOs;
    }

    @Override
    public void update(BrandDTO dto) throws DuplicateKeyException {
        BrandEntity entity = DozerSingletonMapper.getInstance().map(dto, BrandEntity.class);
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        brandLocalBean.update(entity);
    }

    @Override
    public void saveOrUpdate(BrandDTO dto) throws DuplicateKeyException {
        try {
            BrandEntity brandEntity = brandLocalBean.findEqualUnique("brandName", dto.getBrandName());
            brandEntity.setArea(dto.getArea());
            brandEntity.setBizType(dto.getBizType());
            brandEntity.setOwner(dto.getOwner());
            brandEntity.setProcess(dto.getProcess());
            brandEntity.setThumb(dto.getThumb());
            brandEntity.setDescription(dto.getDescription());
            brandEntity.setDetail(dto.getDetail());
            brandEntity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            brandLocalBean.update(brandEntity);
        }catch (ObjectNotFoundException ex){
            BrandEntity entity = DozerSingletonMapper.getInstance().map(dto, BrandEntity.class);
            entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            brandLocalBean.save(entity);
        }
    }
}
