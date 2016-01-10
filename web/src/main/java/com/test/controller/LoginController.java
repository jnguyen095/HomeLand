package com.test.controller;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/11/15
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController extends ApplicationObjectSupport{

    @RequestMapping(value={"/login.html"})
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView("/login");
        return mav;
    }
}
