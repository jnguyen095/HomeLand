package com.test.business.impl;

import com.test.business.BranchManagementLocalBean;
import com.test.business.BranchManagementRemoteBean;
import com.test.domain.BrandEntity;
import com.test.dto.BrandDTO;
import com.test.session.BrandLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/22/2017
 * Time: 5:58 PM
 */
@Stateless(name = "BranchManagementSessionEJB")
public class BranchManagementSessionBean implements BranchManagementRemoteBean, BranchManagementLocalBean {
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
        brandLocalBean.update(entity);
    }
}
