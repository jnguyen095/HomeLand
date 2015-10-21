package com.test.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "Product")
@Entity
public class ProductEntity {
    private Integer productId;
    private String code;
    private String title;
    private String brief;
    private Float price;
    private String priceString;
    private String href;
    private String area;
    private String detail;
    private String thumb;
    private String address;
    private String floor;
    private String room;
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
    private Timestamp expireDate;
    private Timestamp postDate;
    private DirectionEntity direction;
    private BrandEntity brand;
    private CityEntity city;
    private DistrictEntity district;
    private WardEntity ward;
    private StreetEntity street;
    private CategoryEntity category;

    @javax.persistence.Column(name = "ProductID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @javax.persistence.Column(name = "Code")
    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @javax.persistence.Column(name = "Title")
    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @javax.persistence.Column(name = "Brief")
    @Basic
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @javax.persistence.Column(name = "Href")
    @Basic
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @javax.persistence.Column(name = "Price")
    @Basic
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @javax.persistence.Column(name = "PriceString")
    @Basic
    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    @javax.persistence.Column(name = "Area")
    @Basic
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @javax.persistence.Column(name = "Detail", columnDefinition = "TEXT")
    @Basic
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @javax.persistence.Column(name = "Thumb")
    @Basic
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @javax.persistence.Column(name = "Address")
    @Basic
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @javax.persistence.Column(name = "Floor")
    @Basic
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @javax.persistence.Column(name = "Room")
    @Basic
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @javax.persistence.Column(name = "WidthSize")
    @Basic
    public String getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(String widthSize) {
        this.widthSize = widthSize;
    }

    @javax.persistence.Column(name = "LongSize")
    @Basic
    public String getLongSize() {
        return longSize;
    }

    public void setLongSize(String longSize) {
        this.longSize = longSize;
    }

    @javax.persistence.Column(name = "Toilet")
    @Basic
    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
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

    @javax.persistence.Column(name = "ContactName")
    @Basic
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @javax.persistence.Column(name = "ContactPhone")
    @Basic
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @javax.persistence.Column(name = "ContactAddress")
    @Basic
    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    @javax.persistence.Column(name = "ContactMobile")
    @Basic
    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    @javax.persistence.Column(name = "ContactEmail")
    @Basic
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @javax.persistence.Column(name = "ExpireDate")
    @Basic
    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    @javax.persistence.Column(name = "PostDate")
    @Basic
    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "DirectionID")
    public DirectionEntity getDirection() {
        return direction;
    }

    public void setDirection(DirectionEntity direction) {
        this.direction = direction;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "BrandID")
    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "CityID")
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "DistrictID")
    public DistrictEntity getDistrict() {
        return district;
    }

    public void setDistrict(DistrictEntity district) {
        this.district = district;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "WardID")
    public WardEntity getWard() {
        return ward;
    }

    public void setWard(WardEntity ward) {
        this.ward = ward;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "StreetID")
    public StreetEntity getStreet() {
        return street;
    }

    public void setStreet(StreetEntity street) {
        this.street = street;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "CategoryID")
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
