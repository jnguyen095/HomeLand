package com.test;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/30/2017
 * Time: 3:11 PM
 */
@Remote
public interface CrawlerService {
    String crawlerUrl();
    void doCrawler() throws Exception;
    Integer crawlerDeep();
}
