package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "street")
@Entity
public class StreetEntity {
    private Integer streetId;
    private WardEntity ward;
    private String streetName;
    private Float longitude;
    private Float latitude;

    @javax.persistence.Column(name = "StreetID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "WardID")
    public WardEntity getWard() {
        return ward;
    }

    public void setWard(WardEntity ward) {
        this.ward = ward;
    }

    @javax.persistence.Column(name = "StreetName")
    @Basic
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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
