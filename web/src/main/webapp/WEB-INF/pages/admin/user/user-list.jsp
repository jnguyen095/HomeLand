<%@ include file="/common/taglibs.jsp" %>
<%@ page pageEncoding="utf-8" %>
<html>
<head>
    <title><fmt:message key="admin.user.title"/> </title>
</head>
<body>
    <h1 class="page-header"><fmt:message key="admin.user.title"/></h1>
    <div class="panel panel-default">
        <div class="panel-heading"><fmt:message key="user.title"/> </div>
        <div class="panel-body">
            <div class="dataTable_wrapper">
                <div class="row">
                    <div class="col-sm-12">
                        <c:if test="${!empty messageResponse}">
                            <div class="alert alert-success">${messageResponse}</div>
                        </c:if>
                        <c:url var="formUrl" value="/admin/user/list.html"/>
                        <div class="buttons">
                            <a href="<c:url value="/admin/user/edit.html"/>"><button type="button" class="btn btn-primary"><fmt:message key="button.add"/> </button></a>
                        </div>
                        <form:form action="${formUrl}" commandName="items" role="form" id="userGroupForm" cssClass="form-horizontal">
                            <display:table name="items.listResult" cellspacing="0" cellpadding="0" requestURI="${formUrl}"
                                           partialList="true" sort="external" size="${items.totalItems}" defaultsort="-1"
                                           id="tableList" excludedParams="checkList"
                                           pagesize="${items.maxPageItems}" export="false" class="table table-striped table-bordered table-hover dataTable no-footer">

                                <display:column headerClass="table_header" property="userGroup.groupName" sortName="userGroup.groupName" sortable="true" titleKey="usergroup.title" />
                                <display:column headerClass="table_header sorting" property="userName" sortName="userName" sortable="true" titleKey="user.userName" />
                                <display:column headerClass="table_header" property="email" sortName="email" sortable="true" titleKey="user.email" />
                                <display:column headerClass="table_header" property="phone" sortName="phone" sortable="true" titleKey="user.phone" />

                                <display:column headerClass="col-actions" class="col-actions" titleKey="label.action">
                                    <a href="<c:url value="/admin/user/edit.html?pojo.userId=${tableList.userId}"/>"> <i class="fa fa-edit"></i></a>
                                </display:column>

                                <display:setProperty name="paging.banner.item_name" value="người dùng"/>
                                <display:setProperty name="paging.banner.items_name" value="người dùng"/>
                            </display:table>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>