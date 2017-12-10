package com.test.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "brand")
@Entity
public class BrandEntity {
    private Integer brandId;
    private String brandName;
    private String description;
    private String thumb;
    private String bizType;
    private String owner;
    private String process;
    private String area;
    private String price;
    private String detail;
    private Timestamp modifiedDate;


    @javax.persistence.Column(name = "BrandID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    @javax.persistence.Column(name = "BrandName")
    @Basic
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @javax.persistence.Column(name = "Description", columnDefinition = "TEXT")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @javax.persistence.Column(name = "Thumb")
    @Basic
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @javax.persistence.Column(name = "BizType")
    @Basic
    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    @javax.persistence.Column(name = "Owner")
    @Basic
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @javax.persistence.Column(name = "Process")
    @Basic
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @javax.persistence.Column(name = "Area")
    @Basic
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @javax.persistence.Column(name = "Price")
    @Basic
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @javax.persistence.Column(name = "Detail", columnDefinition = "TEXT")
    @Basic
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @javax.persistence.Column(name = "ModifiedDate")
    @Basic
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
