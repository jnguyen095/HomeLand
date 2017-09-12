package com.test.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 3:56 PM
 */
public class NewsDTO implements Serializable {
    private Integer newsId;
    private String title;
    private String thumb;
    private String description;
    private Integer status;
    private Timestamp createdDate;
    private Integer view;
    private String source;
    private String brief;
    private String url;
    private Timestamp crawlerDate;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Timestamp crawlerDate) {
        this.crawlerDate = crawlerDate;
    }
}
