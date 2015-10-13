package com.test.utils;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DozerSingletonMapper extends DozerBeanMapper {
    private transient final Logger logger = Logger.getLogger(DozerSingletonMapper.class);
    private static DozerSingletonMapper instance;

    private DozerSingletonMapper() {
        super();
    }
    public static DozerSingletonMapper getInstance() {
        if(instance == null) {
            instance = new DozerSingletonMapper();
        }
        return instance;
    }
}
