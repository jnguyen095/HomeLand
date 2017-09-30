package com.test.business;

import com.test.dto.BatDongSanDTO;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface ProductManagementRemoteBean {
    Integer[] saveOrUpdate(Integer categoryId, List<BatDongSanDTO> items);
    void updateCrawlerHistory(String siteUrl, Integer saved, Integer exists, Integer error);
}
