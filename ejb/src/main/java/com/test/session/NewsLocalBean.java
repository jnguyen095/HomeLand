package com.test.session;

import com.test.domain.NewsEntity;

import javax.ejb.Local;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 3:09 PM
 */
@Local
public interface NewsLocalBean extends GenericSessionBean<NewsEntity, Long>{
    boolean alreadyCrawler(String url);
}

