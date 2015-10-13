package com.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 4/23/14
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomeController extends ApplicationObjectSupport{


    @RequestMapping("/home.html")
    public ModelAndView home(){
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("name", "Khang Nguyen");
        return mav;
    }

}
