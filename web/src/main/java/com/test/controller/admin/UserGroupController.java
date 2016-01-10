package com.test.controller.admin;

import com.test.Constants;
import com.test.business.UserGroupManagementLocalBean;
import com.test.command.UserGroupCommand;
import com.test.dto.UserGroupDTO;
import com.test.utils.RequestUtil;
import com.test.validator.UserGroupValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.ejb.DuplicateKeyException;
import javax.ejb.ObjectNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserGroupController extends ApplicationObjectSupport {

    @Autowired
    private UserGroupValidator userGroupValidator;
    @Autowired
    private UserGroupManagementLocalBean userGroupManagementLocalBean;

    @RequestMapping("/admin/usergroup/list.html")
    public ModelAndView list(@ModelAttribute(value = Constants.LIST_MODEL_KEY)UserGroupCommand command, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/usergroup/usergroup-list");
        executeSearch(command, request);
        mav.addObject(Constants.LIST_MODEL_KEY, command);
        return mav;
    }

    @RequestMapping("/admin/usergroup/edit.html")
    public ModelAndView test(@ModelAttribute(value = Constants.FORM_MODEL_KEY)UserGroupCommand command, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView("admin/usergroup/usergroup-edit");
        String crudaction = command.getCrudaction();
        if(StringUtils.isNotBlank(crudaction) && crudaction.equals("insert-update")){
            userGroupValidator.validate(command, bindingResult);
            if(!bindingResult.hasErrors()){
                try{
                    userGroupManagementLocalBean.saveOrUpdate(command.getPojo());
                    mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("save.success"));
                    mav = new ModelAndView("redirect:/admin/usergroup/list.html");
                }catch (DuplicateKeyException e){
                    mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("save.error"));
                }
            }
        }
        if(command.getPojo().getUserGroupId() != null){
            try{
                UserGroupDTO edit = userGroupManagementLocalBean.findById(command.getPojo().getUserGroupId());
                command.setPojo(edit);
            }catch (ObjectNotFoundException e){
                mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("error.unexpected"));
            }
        }
        mav.addObject(Constants.FORM_MODEL_KEY, command);
        return mav;
    }

    private void executeSearch(UserGroupCommand command, HttpServletRequest request){
        RequestUtil.initSearchBean(request, command);
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isBlank(command.getSortExpression())){
            command.setSortExpression("groupName");
        }
        command.setSortDirection(Constants.SORT_ASC);
        Object[] results = this.userGroupManagementLocalBean.searchByProperties(properties, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<UserGroupDTO>)results[1]);
        command.setTotalItems(Integer.valueOf(results[0].toString()));
    }
}
