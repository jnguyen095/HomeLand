<%@ include file="/common/taglibs.jsp" %><html>
<head>
    <title><fmt:message key="admin.user.edit"/> </title>
</head>
<body>
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header"><fmt:message key="admin.user.title"/></h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading"><fmt:message key="form.input"/></div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <c:url value="/admin/user/edit.html" var="formUrl"/>
                            <form:form action="${formUrl}" commandName="item">
                                <div class="form-group">
                                    <label><fmt:message key="usergroup.title"/></label>
                                    <form:select path="pojo.userGroup.userGroupId">
                                        <form:options items="${userGroups}" itemValue="userGroupId" itemLabel="groupName"></form:options>
                                    </form:select>
                                    <form:errors path="pojo.userGroup.userGroupId" cssClass="error-inline"/>
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="user.userName"/></label>
                                    <form:input path="pojo.userName" cssClass="form-control"/>
                                    <form:errors path="pojo.userName" cssClass="error-inline"/>
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="user.email"/></label>
                                    <form:input path="pojo.email" class="form-control"/>
                                    <form:errors path="pojo.email" cssClass="error-inline"/>
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="user.password"/></label>
                                    <form:password path="pojo.password" class="form-control"/>
                                    <form:errors path="pojo.password" cssClass="error-inline"/>
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="user.phone"/></label>
                                    <form:input path="pojo.phone" class="form-control"/>
                                    <form:errors path="pojo.phone" cssClass="error-inline"/>
                                </div>

                                <input type="hidden" name="crudaction" value="insert-update"/>
                                <a class="btn btn-default" href="<c:url value="/admin/user/list.html"/>"><fmt:message key="button.back"/></a>
                                <button type="submit" class="btn btn-primary"><fmt:message key="button.submit"/></button>
                                <form:hidden path="pojo.userId"/>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>