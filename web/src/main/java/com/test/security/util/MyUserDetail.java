package com.test.security.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;

public class MyUserDetail extends User {

	public MyUserDetail(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}
	
	private Long userId;

    private String userName;
	
	private String displayName;
	
	private String email;

    private String role;

    private String mobileNumber;

    private Long departmentId;

    private String firstName;

    private String lastName;

    private String middleName;

    private Long portalId;

    private String portalCode;

    private String avatar;

    private Timestamp createdDate;

    private String portalName;

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public String getAvatar() {
        return avatar == null ? "user2-160x160.jpg" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Long getPortalId() {
        return portalId;
    }

    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getPortalCode() {
        return portalCode;
    }

    public void setPortalCode(String portalCode) {
        this.portalCode = portalCode;
    }
}
