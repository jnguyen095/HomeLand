package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "Brand")
@Entity
public class BrandEntity {
    private Integer brandId;
    private String brandName;
    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrandEntity that = (BrandEntity) o;

        if (brandId != null ? !brandId.equals(that.brandId) : that.brandId != null) return false;
        if (brandName != null ? !brandName.equals(that.brandName) : that.brandName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = brandId != null ? brandId.hashCode() : 0;
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
