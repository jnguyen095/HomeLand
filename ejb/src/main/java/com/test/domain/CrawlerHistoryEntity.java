package com.test.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "CrawlerHistory")
@Entity
public class CrawlerHistoryEntity {
    private Integer crawlerHistoryId;
    private CategoryEntity category;
    private Integer added;
    private Integer updated;
    private Integer error;
    private Timestamp updatedDate;

    @javax.persistence.Column(name = "CrawlerHistoryID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getCrawlerHistoryId() {
        return crawlerHistoryId;
    }

    public void setCrawlerHistoryId(Integer crawlerHistoryId) {
        this.crawlerHistoryId = crawlerHistoryId;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "CategoryID")
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @javax.persistence.Column(name = "Added")
    @Basic
    public Integer getAdded() {
        return added;
    }

    public void setAdded(Integer added) {
        this.added = added;
    }

    @javax.persistence.Column(name = "Updated")
    @Basic
    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    @javax.persistence.Column(name = "Error")
    @Basic
    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    @javax.persistence.Column(name = "UpdatedDate")
    @Basic
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}

