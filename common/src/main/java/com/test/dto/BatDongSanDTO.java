package com.test.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/11/15
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatDongSanDTO implements Serializable {

    private List<String> images;
    private String postDateStr;
    private String expireDateStr;
    private String brief;
    private String detail;
    private String code;
    private String title;
    private String href;
    private String priceString;
    private String area;
    private String cityDist;
    private String thumb;
    private String address;
    private String floor;
    private String room;
    private String widthSize;
    private String longSize;
    private String group;
    private String toilet;
    private String longitude;
    private String latitude;
    private String directionString;
    private String brandString;
    private String wardString;
    private String streetString;


    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private String contactEmail;
    private String contactMobile;

    public String getWardString() {
        return wardString;
    }

    public void setWardString(String wardString) {
        this.wardString = wardString;
    }

    public String getStreetString() {
        return streetString;
    }

    public void setStreetString(String streetString) {
        this.streetString = streetString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCityDist() {
        return cityDist;
    }

    public void setCityDist(String cityDist) {
        this.cityDist = cityDist;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(String widthSize) {
        this.widthSize = widthSize;
    }

    public String getLongSize() {
        return longSize;
    }

    public void setLongSize(String longSize) {
        this.longSize = longSize;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPostDateStr() {
        return postDateStr;
    }

    public void setPostDateStr(String postDateStr) {
        this.postDateStr = postDateStr;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        this.expireDateStr = expireDateStr;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getDirectionString() {
        return directionString;
    }

    public void setDirectionString(String directionString) {
        this.directionString = directionString;
    }

    public String getBrandString() {
        return brandString;
    }

    public void setBrandString(String brandString) {
        this.brandString = brandString;
    }
}
