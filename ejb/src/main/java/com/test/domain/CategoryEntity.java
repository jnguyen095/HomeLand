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
@javax.persistence.Table(name = "Category")
@Entity
public class CategoryEntity {
    private Integer categoryId;
    private CategoryEntity parent;
    private String name;
    private String url;
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

    @javax.persistence.Column(name = "Url")
    @javax.persistence.Basic
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntity that = (CategoryEntity) o;

        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = categoryId != null ? categoryId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }
}
