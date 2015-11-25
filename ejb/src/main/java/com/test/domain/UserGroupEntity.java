package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/31/15
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "UserGroup")
@Entity
public class UserGroupEntity {
    private Integer userGroupId;
    private String code;
    private String groupName;

    @javax.persistence.Column(name = "UserGroupID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    @javax.persistence.Column(name = "Code")
    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @javax.persistence.Column(name = "GroupName")
    @Basic
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroupEntity that = (UserGroupEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
        if (userGroupId != null ? !userGroupId.equals(that.userGroupId) : that.userGroupId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userGroupId != null ? userGroupId.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}
