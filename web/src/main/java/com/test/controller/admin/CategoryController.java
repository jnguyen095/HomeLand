package com.test.controller.admin;

import com.test.Constants;
import com.test.business.CategoryManagementLocalBean;
import com.test.command.CategoryCommand;
import com.test.dto.CategoryDTO;
import com.test.utils.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ModelAndView list(@ModelAttribute(value = Constants.LIST_MODEL_KEY)CategoryCommand command, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/category/category-list");
        executeSearch(command, request);
        mav.addObject("name", "Khang Nguyen");
        mav.addObject(Constants.LIST_MODEL_KEY, command);
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

    private void executeSearch(CategoryCommand command, HttpServletRequest request) {
        RequestUtil.initSearchBean(request, command);
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isBlank(command.getSortExpression())){
            command.setSortExpression("name");
        }
        command.setSortDirection(Constants.SORT_ASC);
        Object[] results = this.categoryManagementLocalBean.searchByProperties(properties, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<CategoryDTO>)results[1]);
        command.setTotalItems(Integer.valueOf(results[0].toString()));
    }
}
