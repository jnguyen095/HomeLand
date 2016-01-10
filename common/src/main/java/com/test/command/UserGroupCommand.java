package com.test.command;

import com.test.dto.UserGroupDTO;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupCommand extends AbstractCommand<UserGroupDTO>{
    public UserGroupCommand(){
        this.pojo = new UserGroupDTO();
    }
}
