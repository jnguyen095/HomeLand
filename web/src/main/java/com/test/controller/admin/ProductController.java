package com.test.controller.admin;

import com.test.Constants;
import com.test.business.ProductManagementLocalBean;
import com.test.command.CategoryCommand;
import com.test.command.ProductCommand;
import com.test.dto.CategoryDTO;
import com.test.dto.ProductDTO;
import com.test.utils.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 11/25/15
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ProductController extends ApplicationObjectSupport{

    @Autowired
    private ProductManagementLocalBean productManagementLocalBean;

    @RequestMapping("/admin/product/list.html")
    public ModelAndView list(@ModelAttribute(value = Constants.LIST_MODEL_KEY)ProductCommand command, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/product/product-list");
        executeSearch(command, request);
        mav.addObject(Constants.LIST_MODEL_KEY, command);
        productManagementLocalBean.mergeProduct(105, 55);
        return mav;
    }

    private void executeSearch(ProductCommand command, HttpServletRequest request) {
        RequestUtil.initSearchBean(request, command);
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isBlank(command.getSortExpression())){
            command.setSortExpression("title");
        }
        command.setSortDirection(Constants.SORT_ASC);
        Object[] results = this.productManagementLocalBean.searchByProperties(properties, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<ProductDTO>)results[1]);
        command.setTotalItems(Integer.valueOf(results[0].toString()));
    }
}
