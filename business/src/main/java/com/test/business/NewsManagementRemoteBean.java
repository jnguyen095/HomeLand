package com.test.business;

import com.test.dto.NewsDTO;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 4:21 PM
 */
@Remote
public interface NewsManagementRemoteBean {
    Integer[] saveItems(List<NewsDTO> items);
}
