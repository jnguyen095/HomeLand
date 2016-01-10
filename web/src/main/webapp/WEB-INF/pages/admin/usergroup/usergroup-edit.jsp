<%@ include file="/common/taglibs.jsp" %><html>
<head>
    <title><fmt:message key="admin.usergroup.edit"/> </title>
</head>
<body>
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header"><fmt:message key="admin.usergroup.title"/></h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading"><fmt:message key="form.input"/></div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <c:url value="/admin/usergroup/edit.html" var="formUrl"/>
                            <form:form action="${formUrl}" commandName="item">
                                <div class="form-group">
                                    <label><fmt:message key="label.code"/></label>
                                    <form:input path="pojo.code" cssClass="form-control"/>
                                    <form:errors path="pojo.code" cssClass="error-inline"/>
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="label.name"/></label>
                                    <form:input path="pojo.groupName" class="form-control"/>
                                    <form:errors path="pojo.groupName" cssClass="error-inline"/>
                                </div>

                                <input type="hidden" name="crudaction" value="insert-update"/>
                                <a class="btn btn-default" href="<c:url value="/admin/usergroup/list.html"/>"><fmt:message key="button.back"/></a>
                                <button type="submit" class="btn btn-primary"><fmt:message key="button.submit"/></button>
                                <form:hidden path="pojo.userGroupId"/>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>