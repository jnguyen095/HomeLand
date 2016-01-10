package com.test.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 12/12/15
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupDTO implements Serializable {
    private Integer userGroupId;
    private String code;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
