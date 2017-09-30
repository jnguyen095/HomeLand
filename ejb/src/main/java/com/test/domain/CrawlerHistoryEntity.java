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
@javax.persistence.Table(name = "crawlerhistory")
@Entity
public class CrawlerHistoryEntity {
    private Integer crawlerHistoryId;
    private String siteUrl;
    private Integer added;
    private Integer skip;
    private Integer error;
    private Timestamp crawlerDate;

    @javax.persistence.Column(name = "CrawlerHistoryID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getCrawlerHistoryId() {
        return crawlerHistoryId;
    }

    public void setCrawlerHistoryId(Integer crawlerHistoryId) {
        this.crawlerHistoryId = crawlerHistoryId;
    }

    @javax.persistence.Column(name = "Added")
    @Basic
    public Integer getAdded() {
        return added;
    }

    public void setAdded(Integer added) {
        this.added = added;
    }

    @javax.persistence.Column(name = "Skip")
    @Basic
    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    @javax.persistence.Column(name = "Error")
    @Basic
    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    @javax.persistence.Column(name = "SiteUrl")
    @Basic
    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    @javax.persistence.Column(name = "CrawlerDate")
    @Basic
    public Timestamp getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Timestamp crawlerDate) {
        this.crawlerDate = crawlerDate;
    }
}

