<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <title><fmt:message key="admin.product.title"/> </title>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading"><fmt:message key="product.title"/> </div>
    <div class="panel-body">
        <div class="dataTable_wrapper">
            <div class="row">
                <c:url var="formUrl" value="/admin/product/list.html"/>
                <form:form action="${formUrl}" commandName="items" role="form" id="brandForm" cssClass="form-horizontal">
                    <display:table name="items.listResult" cellspacing="0" cellpadding="0" requestURI="${formUrl}"
                                   partialList="true" sort="external" size="${items.totalItems}" defaultsort="-1"
                                   id="tableList" excludedParams="checkList"
                                   pagesize="${items.maxPageItems}" export="false" class="table table-striped table-bordered table-hover dataTable no-footer">
                        <display:column headerClass="table_header sorting" property="title" sortName="title" sortable="true" titleKey="product.name" />
                        <display:column headerClass="table_header sorting" sortName="price" sortable="price" titleKey="product.price">
                            <fmt:formatNumber value="${tableList.price}"/>
                        </display:column>

                        <display:column headerClass="col-actions" class="col-actions" titleKey="label.action">
                            <a class="addEditBrand" brandId="${tableList.productId}"> <i class="fa fa-edit"></i></a>
                        </display:column>

                        <display:setProperty name="paging.banner.item_name" value="product"/>
                        <display:setProperty name="paging.banner.items_name" value="products"/>
                    </display:table>
                </form:form>
            </div>
        </div>
    </div>
</div>

</body>
</html>