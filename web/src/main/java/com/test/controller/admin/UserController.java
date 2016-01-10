package com.test.controller.admin;

import com.test.Constants;
import com.test.business.UserGroupManagementLocalBean;
import com.test.business.UserManagementLocalBean;
import com.test.command.UserCommand;
import com.test.dto.UserDTO;
import com.test.dto.UserGroupDTO;
import com.test.security.DesEncrypterUtils;
import com.test.utils.RequestUtil;
import com.test.validator.UserValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.DuplicateKeyException;
import javax.ejb.ObjectNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserController extends ApplicationObjectSupport {
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserManagementLocalBean userManagementLocalBean;
    @Autowired
    private UserGroupManagementLocalBean userGroupManagementLocalBean;

    @RequestMapping("/admin/user/list.html")
    public ModelAndView list(@ModelAttribute(value = Constants.LIST_MODEL_KEY)UserCommand command, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("admin/user/user-list");
        executeSearch(command, request);
        mav.addObject(Constants.LIST_MODEL_KEY, command);
        return mav;
    }

    @RequestMapping("/admin/user/edit.html")
    public ModelAndView test(@ModelAttribute(value = Constants.FORM_MODEL_KEY)UserCommand command, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView("admin/user/user-edit");
        String crudaction = command.getCrudaction();
        if(StringUtils.isNotBlank(crudaction) && crudaction.equals("insert-update")){
            userValidator.validate(command, bindingResult);
            if(!bindingResult.hasErrors()){
                try{
                    String passwordEncode = DesEncrypterUtils.getInstance().encrypt(command.getPojo().getPassword());
                    command.getPojo().setPassword(passwordEncode);
                    userManagementLocalBean.saveOrUpdate(command.getPojo());
                    mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("save.success"));
                    mav = new ModelAndView("redirect:/admin/user/list.html");
                    return mav;
                }catch (DuplicateKeyException e){
                    mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("save.error"));
                }
            }
        }
        if(command.getPojo().getUserId() != null){
            try{
                UserDTO edit = userManagementLocalBean.findById(command.getPojo().getUserId());
                String passwordDecode = DesEncrypterUtils.getInstance().decrypt(edit.getPassword());
                edit.setPassword(passwordDecode);
                command.setPojo(edit);
            }catch (ObjectNotFoundException e){
                mav.addObject(Constants.MESSAGE_RESPONSE, this.getMessageSourceAccessor().getMessage("error.unexpected"));
            }
        }
        List<UserGroupDTO> userGroups = userGroupManagementLocalBean.findAll();
        mav.addObject("userGroups", userGroups);
        mav.addObject(Constants.FORM_MODEL_KEY, command);
        return mav;
    }

    private void executeSearch(UserCommand command, HttpServletRequest request){
        RequestUtil.initSearchBean(request, command);
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isBlank(command.getSortExpression())){
            command.setSortExpression("createdDate");
        }
        command.setSortDirection(Constants.SORT_ASC);
        Object[] results = this.userManagementLocalBean.searchByProperties(properties, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<UserDTO>)results[1]);
        command.setTotalItems(Integer.valueOf(results[0].toString()));
    }
    
}
