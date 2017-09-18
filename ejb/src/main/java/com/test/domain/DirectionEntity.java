package com.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/15/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "direction")
@Entity
public class DirectionEntity {
    private Integer directionId;
    private String directionName;

    @javax.persistence.Column(name = "DirectionID")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    @javax.persistence.Column(name = "DirectionName")
    @Basic
    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectionEntity that = (DirectionEntity) o;

        if (directionId != null ? !directionId.equals(that.directionId) : that.directionId != null) return false;
        if (directionName != null ? !directionName.equals(that.directionName) : that.directionName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = directionId != null ? directionId.hashCode() : 0;
        result = 31 * result + (directionName != null ? directionName.hashCode() : 0);
        return result;
    }
}
