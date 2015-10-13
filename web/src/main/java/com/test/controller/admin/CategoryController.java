package com.test.controller.admin;

import com.test.business.CategoryManagementLocalBean;
import com.test.command.CategoryCommand;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CategoryController extends ApplicationObjectSupport{
    @EJB
    private CategoryManagementLocalBean categoryManagementLocalBean;

    @RequestMapping("/admin/category/list.html")
    public ModelAndView list(){
        ModelAndView mav = new ModelAndView("admin/category/category-list");
        mav.addObject("name", "Khang Nguyen");
        return mav;
    }

    @RequestMapping("/admin/category/edit.html")
    public ModelAndView edit(@ModelAttribute("command")CategoryCommand command){
        ModelAndView mav = new ModelAndView("admin/category/category-edit");
        if(command.getCrudaction() != null && command.getCrudaction().equals("insert-update")){
            //categoryManagementLocalBean.save(command.getPojo());
        }
        mav.addObject("name", "Khang Nguyen");
        return mav;
    }
}
