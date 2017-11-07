package com.test.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/16/15
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryDTO implements Serializable{
    private Integer categoryId;
    private String name;
    private String batdongsanUrl;
    private String muabannhadatUrl;
    private String dothiUrl;
    private String cafelandUrl;
    private Integer active;
    private CategoryDTO parent;
    private Integer crawled;

    public CategoryDTO getParent() {
        return parent;
    }

    public void setParent(CategoryDTO parent) {
        this.parent = parent;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getCrawled() {
        return crawled;
    }

    public void setCrawled(Integer crawled) {
        this.crawled = crawled;
    }

    public String getBatdongsanUrl() {
        return batdongsanUrl;
    }

    public void setBatdongsanUrl(String batdongsanUrl) {
        this.batdongsanUrl = batdongsanUrl;
    }

    public String getMuabannhadatUrl() {
        return muabannhadatUrl;
    }

    public void setMuabannhadatUrl(String muabannhadatUrl) {
        this.muabannhadatUrl = muabannhadatUrl;
    }

    public String getDothiUrl() {
        return dothiUrl;
    }

    public void setDothiUrl(String dothiUrl) {
        this.dothiUrl = dothiUrl;
    }

    public String getCafelandUrl() {
        return cafelandUrl;
    }

    public void setCafelandUrl(String cafelandUrl) {
        this.cafelandUrl = cafelandUrl;
    }
}
