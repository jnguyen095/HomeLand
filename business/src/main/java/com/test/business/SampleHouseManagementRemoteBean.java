package com.test.business;

import com.test.dto.SampleHouseDTO;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 8:02 AM
 */
@Remote
public interface SampleHouseManagementRemoteBean {
    Integer[] saveItems(List<SampleHouseDTO> items);
}
