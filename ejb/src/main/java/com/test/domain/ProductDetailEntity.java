package com.test.domain;

import javax.persistence.*;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/25/2017
 * Time: 5:12 PM
 */
@Entity
@Table(name = "productdetail")
public class ProductDetailEntity {
    private Integer productDetailId;
    private String detail;
    private String room;
    private String floor;
    private String widthSize;
    private String longSize;
    private String toilet;
    private Float longitude;
    private Float latitude;
    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private String contactMobile;
    private String contactEmail;
    private DirectionEntity direction;
    private String source;
    private ProductEntity product;
    private String href;

    @Id
    @Column(name = "ProductDetailID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(Integer productDetailId) {
        this.productDetailId = productDetailId;
    }

    @Basic
    @Column(name = "Detail", columnDefinition = "TEXT")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "Room")
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Basic
    @Column(name = "Floor")
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Basic
    @Column(name = "WidthSize")
    public String getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(String widthSize) {
        this.widthSize = widthSize;
    }

    @Basic
    @Column(name = "LongSize")
    public String getLongSize() {
        return longSize;
    }

    public void setLongSize(String longSize) {
        this.longSize = longSize;
    }

    @Basic
    @Column(name = "Toilet")
    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    @Basic
    @Column(name = "Longitude")
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "Latitude")
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "ContactName")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Basic
    @Column(name = "ContactPhone")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Basic
    @Column(name = "ContactAddress")
    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    @Basic
    @Column(name = "ContactMobile")
    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    @Basic
    @Column(name = "ContactEmail")
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "DirectionID")
    public DirectionEntity getDirection() {
        return direction;
    }

    public void setDirection(DirectionEntity direction) {
        this.direction = direction;
    }

    @Basic
    @Column(name = "Source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "ProductID")
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Basic
    @Column(name = "Href")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
