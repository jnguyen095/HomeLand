package com.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/15/15
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "category")
@Entity
public class CategoryEntity {
    private Integer categoryId;
    private CategoryEntity parent;
    private String name;
    private String batdongsanUrl;
    private String muabannhadatUrl;
    private Integer active;
    private Integer crawled;

    @javax.persistence.Column(name = "CategoryID")
    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @javax.persistence.Column(name = "CatName")
    @javax.persistence.Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @javax.persistence.Column(name = "Active")
    @javax.persistence.Basic
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @javax.persistence.Column(name = "Crawled")
    @javax.persistence.Basic
    public Integer getCrawled() {
        return crawled;
    }

    public void setCrawled(Integer crawled) {
        this.crawled = crawled;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "ParentId", referencedColumnName = "CategoryID")
    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    @javax.persistence.Column(name = "MuabannhadatUrl")
    @javax.persistence.Basic
    public String getMuabannhadatUrl() {
        return muabannhadatUrl;
    }

    public void setMuabannhadatUrl(String muabannhadatUrl) {
        this.muabannhadatUrl = muabannhadatUrl;
    }

    @javax.persistence.Column(name = "BatdongsanUrl")
    @javax.persistence.Basic
    public String getBatdongsanUrl() {
        return batdongsanUrl;
    }

    public void setBatdongsanUrl(String batdongsanUrl) {
        this.batdongsanUrl = batdongsanUrl;
    }
}
