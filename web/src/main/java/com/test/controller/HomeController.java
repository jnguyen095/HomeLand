package com.test.controller;


import com.test.business.ProductManagementLocalBean;
import com.test.command.ProductCommand;
import com.test.dto.ProductDTO;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 4/23/14
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomeController extends ApplicationObjectSupport{

    @Autowired
    private ProductManagementLocalBean productManagementLocalBean;

    @RequestMapping("/home.html")
    public ModelAndView home(@ModelAttribute(value = "item")ProductCommand command){
        ModelAndView mav = new ModelAndView("home");
        Map<String, Object> properties = new HashedMap();
        Object[] objects = productManagementLocalBean.searchByProperties(properties, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        mav.addObject("products", (List<ProductDTO>)objects[1]);
        mav.addObject("name", "Khang Nguyen");
        return mav;
    }

}
