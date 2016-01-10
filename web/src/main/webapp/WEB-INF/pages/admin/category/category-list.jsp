<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <title><fmt:message key="admin.category.title"/> </title>
</head>
<body>
    <h1 class="page-header"><fmt:message key="category.title"/></h1>
    <div class="panel panel-default">
        <div class="panel-heading"><fmt:message key="category.list"/> </div>
        <div class="panel-body">
            <div class="dataTable_wrapper">
                <div class="row">
                    <div class="col-sm-12">
                        <c:url var="formUrl" value="/admin/category/list.html"/>
                        <form:form action="${formUrl}" commandName="items" role="form" id="brandForm" cssClass="form-horizontal">
                            <display:table name="items.listResult" cellspacing="0" cellpadding="0" requestURI="${formUrl}"
                                           partialList="true" sort="external" size="${items.totalItems}" defaultsort="-1"
                                           id="tableList" excludedParams="checkList"
                                           pagesize="${items.maxPageItems}" export="false" class="table table-striped table-bordered table-hover dataTable no-footer">
                                <display:column headerClass="table_header sorting" property="name" sortName="code" sortable="true" titleKey="label.name" />
                                <display:column headerClass="table_header sorting" sortName="name" sortable="true" titleKey="category.url">
                                    <a href="${tableList.url}" target="_blank">${tableList.url}</a>
                                </display:column>

                                <display:column headerClass="col-actions" class="col-actions" titleKey="label.action">
                                    <a class="addEditBrand" brandId="${tableList.categoryId}"> <i class="fa fa-edit"></i></a>
                                </display:column>

                                <display:setProperty name="paging.banner.item_name" value="category"/>
                                <display:setProperty name="paging.banner.items_name" value="categories"/>
                            </display:table>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>