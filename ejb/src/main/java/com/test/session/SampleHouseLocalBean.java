package com.test.session;

import com.test.domain.SampleHouseEntity;

import javax.ejb.Local;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 7:59 AM
 */
@Local
public interface SampleHouseLocalBean extends GenericSessionBean<SampleHouseEntity, Long> {
    boolean alreadyCrawler(String url);
}
