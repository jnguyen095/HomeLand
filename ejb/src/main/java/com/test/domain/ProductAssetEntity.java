package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "productasset")
@Entity
public class ProductAssetEntity {
    private Integer productAssetId;
    private ProductEntity product;
    private String url;
    private String orgUrl;

    @javax.persistence.Column(name = "ProductAssetID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getProductAssetId() {
        return productAssetId;
    }

    public void setProductAssetId(Integer productAssetId) {
        this.productAssetId = productAssetId;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "ProductID")
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @javax.persistence.Column(name = "Url")
    @Basic
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @javax.persistence.Column(name = "OrgUrl")
    @Basic
    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }
}
