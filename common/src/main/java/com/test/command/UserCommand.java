package com.test.command;

import com.test.dto.UserDTO;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 1/8/16
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCommand extends AbstractCommand<UserDTO> {
    public UserCommand(){
        this.pojo = new UserDTO();
    }
}

