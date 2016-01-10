package com.test.validator;

import com.test.business.UserGroupManagementLocalBean;
import com.test.command.UserGroupCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserGroupValidator implements Validator {

    @Autowired
    private UserGroupManagementLocalBean userGroupManagementLocalBean;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserGroupCommand.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserGroupCommand command = (UserGroupCommand)o;
        validateRequired(command, errors);
        checkUniqueCode(command, errors);
    }

    private void checkUniqueCode(UserGroupCommand command, Errors errors) {
        String code = command.getPojo().getCode();
        Integer id = command.getPojo().getUserGroupId();
        if(StringUtils.isNotBlank(code)){
            Boolean isDuplicated = userGroupManagementLocalBean.isDuplicated(code, id);
            if(isDuplicated){
                errors.rejectValue("pojo.code", "duplicated.message", new Object[]{code}, null);
            }
        }
    }

    private void validateRequired(UserGroupCommand command, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.code", "validator.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.groupName", "validator.required");
    }
}
