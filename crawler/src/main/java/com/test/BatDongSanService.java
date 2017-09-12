package com.test;

import com.test.dto.CategoryTreeDTO;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/10/15
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface BatDongSanService {
    void updateMainCategory();
    void crawlerNews();
}
