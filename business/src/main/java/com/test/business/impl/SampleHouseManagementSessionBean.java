package com.test.business.impl;

import com.test.business.SampleHouseManagementLocalBean;
import com.test.business.SampleHouseManagementRemoteBean;
import com.test.domain.SampleHouseEntity;
import com.test.dto.SampleHouseDTO;
import com.test.session.SampleHouseLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 8:04 AM
 */
@Stateless(name = "SampleHouseManagementSessionEJB")
public class SampleHouseManagementSessionBean implements SampleHouseManagementLocalBean, SampleHouseManagementRemoteBean {
    @EJB
    private SampleHouseLocalBean sampleHouseLocalBean;

    @Override
    public Integer[] saveItems(List<SampleHouseDTO> items) {
        int success = 0, exists = 0, error = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for(SampleHouseDTO sampleHouseDTO : items){
            SampleHouseEntity entity = DozerSingletonMapper.getInstance().map(sampleHouseDTO, SampleHouseEntity.class);
            entity.setCrawlerDate(now);
            try {
                if(!sampleHouseLocalBean.alreadyCrawler(entity.getUrl())) {
                    sampleHouseLocalBean.save(entity);
                    success++;
                }else{
                    exists++;
                }
            }catch (Exception e){
                error++;
            }
        }

        Integer[] result = new Integer[3];
        result[0] = success;
        result[1] = exists;
        result[2] = error;
        return result;
    }
}
