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
    private String area;
    private String thumb;
    private Timestamp expireDate;
    private Timestamp postDate;
    private Timestamp modifiedDate;
    private BrandEntity brand;
    private CityEntity city;
    private DistrictEntity district;
    private WardEntity ward;
    private String street;
    private CategoryEntity category;
    private Integer view;
    private UnitEntity unit;
    private String address;

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

    @javax.persistence.Column(name = "Thumb")
    @Basic
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    @Basic
    @javax.persistence.Column(name = "Street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
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

    @Basic
    @javax.persistence.Column(name = "View")
    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    @ManyToOne
    @javax.persistence.JoinColumn(name = "UnitID")
    public UnitEntity getUnit() {
        return unit;
    }

    public void setUnit(UnitEntity unit) {
        this.unit = unit;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @javax.persistence.Column(name = "Address")
    @Basic
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
