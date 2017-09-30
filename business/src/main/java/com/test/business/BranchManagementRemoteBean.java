package com.test.business;

import com.test.dto.BrandDTO;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/22/2017
 * Time: 6:00 PM
 */
@Remote
public interface BranchManagementRemoteBean {
    List<BrandDTO> findAll();

    void update(BrandDTO dto) throws DuplicateKeyException;
}
