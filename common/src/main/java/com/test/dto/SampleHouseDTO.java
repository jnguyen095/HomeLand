package com.test.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 8:03 AM
 */
public class SampleHouseDTO implements Serializable {
    private Integer sampleHouseId;
    private String title;
    private String brief;
    private String thumb;
    private String description;
    private Integer status;
    private Timestamp createdDate;
    private Integer view;
    private String source;
    private Timestamp crawlerDate;
    private String url;

    public Integer getSampleHouseId() {
        return sampleHouseId;
    }

    public void setSampleHouseId(Integer sampleHouseId) {
        this.sampleHouseId = sampleHouseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Timestamp getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Timestamp crawlerDate) {
        this.crawlerDate = crawlerDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
