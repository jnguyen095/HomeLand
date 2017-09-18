package com.test.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/14/2017
 * Time: 7:57 AM
 */
@Entity
@Table(name = "samplehouse")
public class SampleHouseEntity {
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

    @Id
    @Column(name = "SampleHouseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getSampleHouseId() {
        return sampleHouseId;
    }

    public void setSampleHouseId(Integer sampleHouseId) {
        this.sampleHouseId = sampleHouseId;
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
    @Column(name = "Brief")
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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
    @Column(name = "CrawlerDate")
    public Timestamp getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Timestamp crawlerDate) {
        this.crawlerDate = crawlerDate;
    }

    @Basic
    @Column(name = "Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleHouseEntity that = (SampleHouseEntity) o;

        if (sampleHouseId != null ? !sampleHouseId.equals(that.sampleHouseId) : that.sampleHouseId != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (brief != null ? !brief.equals(that.brief) : that.brief != null) return false;
        if (thumb != null ? !thumb.equals(that.thumb) : that.thumb != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (view != null ? !view.equals(that.view) : that.view != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (crawlerDate != null ? !crawlerDate.equals(that.crawlerDate) : that.crawlerDate != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sampleHouseId != null ? sampleHouseId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (brief != null ? brief.hashCode() : 0);
        result = 31 * result + (thumb != null ? thumb.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (view != null ? view.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (crawlerDate != null ? crawlerDate.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
