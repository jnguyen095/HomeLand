package com.test.business.impl;

import com.test.business.NewsManagementLocalBean;
import com.test.business.NewsManagementRemoteBean;
import com.test.domain.NewsEntity;
import com.test.dto.NewsDTO;
import com.test.session.NewsLocalBean;
import com.test.utils.DozerSingletonMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 4:22 PM
 */
@Stateless(name = "NewsManagementSessionEJB")
public class NewsManagementSessionBean implements NewsManagementLocalBean, NewsManagementRemoteBean {
    @EJB
    private NewsLocalBean newsLocalBean;

    @Override
    public Integer[] saveItems(List<NewsDTO> items) {
        int success = 0, exists = 0, error = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for(NewsDTO newsDTO : items){
            NewsEntity entity = DozerSingletonMapper.getInstance().map(newsDTO, NewsEntity.class);
            entity.setCrawlerDate(now);
            try {
                if(!newsLocalBean.alreadyCrawler(entity.getUrl())) {
                    newsLocalBean.save(entity);
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
