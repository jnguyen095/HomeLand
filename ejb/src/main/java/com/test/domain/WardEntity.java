package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "ward")
@Entity
public class WardEntity {
    private Integer wardId;
    private DistrictEntity district;
    private String wardName;
    private Float longitude;
    private Float latitude;

    @javax.persistence.Column(name = "WardID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "DistrictID")
    public DistrictEntity getDistrict() {
        return district;
    }

    public void setDistrict(DistrictEntity district) {
        this.district = district;
    }

    @javax.persistence.Column(name = "WardName")
    @Basic
    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    @javax.persistence.Column(name = "Longitude")
    @Basic
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @javax.persistence.Column(name = "Latitude")
    @Basic
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}
