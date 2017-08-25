package com.test.domain;

import javax.persistence.*;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/25/2017
 * Time: 5:12 PM
 */
@Entity
@Table(name = "unit")
public class UnitEntity {
    private Integer unitId;
    private String title;
    private Integer displayOrder;

    @Id
    @Column(name = "UnitID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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
    @Column(name = "DisplayOrder")
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitEntity that = (UnitEntity) o;

        if (unitId != that.unitId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = unitId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        return result;
    }
}
