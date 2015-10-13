<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="admin.category.edit"/> </title>
</head>
<body>
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header"><fmt:message key="admin.category.edit"/></h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading"><fmt:message key="form.input"/></div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <c:url value="/admin/category/edit.html" var="formUrl"/>
                            <form:form action="${formUrl}" commandName="command">
                                <div class="form-group">
                                    <label><fmt:message key="category.name"/></label>
                                    <input name="name" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="category.url"/></label>
                                    <input name="url" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label><fmt:message key="label.status"/></label>
                                    <label class="radio-inline">
                                        <input type="radio" name="active" value="0" checked><fmt:message key="label.active"/>
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="active" value="1"><fmt:message key="label.inactive"/>
                                    </label>
                                </div>

                                <input type="hidden" name="crudaction" value="insert-update"/>
                                <button type="reset" class="btn btn-default"><fmt:message key="button.reset"/></button>
                                <button type="submit" class="btn btn-primary"><fmt:message key="button.submit"/></button>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>