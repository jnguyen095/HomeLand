package com.test.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/11/2017
 * Time: 3:06 PM
 */
@Entity
@Table(name = "news")
public class NewsEntity {
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

    @Id
    @Column(name = "NewsID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    @Basic
    @Column(name = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "Thumb")
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Basic
    @Column(name = "Description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "Status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "CreatedDate")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "View")
    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    @Basic
    @Column(name = "Source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Basic
    @Column(name = "Brief")
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Basic
    @Column(name = "Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "CrawlerDate")
    public Timestamp getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Timestamp crawlerDate) {
        this.crawlerDate = crawlerDate;
    }
}
