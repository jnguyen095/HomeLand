package com.test.validator;

import com.test.business.UserManagementLocalBean;
import com.test.command.UserCommand;
import com.test.command.UserGroupCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserValidator extends ApplicationObjectSupport implements Validator {

    @Autowired
    private UserManagementLocalBean userManagementLocalBean;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserCommand.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserCommand command = (UserCommand)o;
        validateRequired(command, errors);
        checkUniqueCode(command, errors);
    }

    private void checkUniqueCode(UserCommand command, Errors errors) {
        String userName = command.getPojo().getUserName();
        Integer id = command.getPojo().getUserId();
        if(StringUtils.isNotBlank(userName)){
            Boolean isDuplicated = userManagementLocalBean.isDuplicated(userName, id);
            if(isDuplicated){
                errors.rejectValue("pojo.userName", "duplicated.message", new Object[]{userName}, null);
            }
        }
    }

    private void validateRequired(UserCommand command, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.userName", "validator.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.email", "validator.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.password", "validator.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pojo.phone", "validator.required");
    }
}
