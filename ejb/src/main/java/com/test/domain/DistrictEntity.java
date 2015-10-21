package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "District")
@Entity
public class DistrictEntity {
    private Integer districtId;
    private CityEntity city;
    private String districtName;
    private Float longitude;
    private Float latitude;

    @javax.persistence.Column(name = "DistrictID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "CityID")
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    @javax.persistence.Column(name = "DistrictName")
    @Basic
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @javax.persistence.Column(name = "Longitude")
    @Basic
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @javax.persistence.Column(name = "Latitude"
    )
    @Basic
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}
