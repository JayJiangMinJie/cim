package com.geovis.luoning.bo;

import org.postgis.Geometry;
import org.postgis.Polygon;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ConstructionPlanBO implements Serializable,Cloneable{
    private Integer id;
    private String planCode ;
    private String landUse ;
    private String landUseCode ;
    private String address ;
    private Integer landArea ;
    private String imagesPath ;
    private LocalDateTime createTime;
    private Geometry planGeo ;
    private String constructionStatus ;
    private String geoString ;

    public String getGeoString() {
        return geoString;
    }

    public void setGeoString(String geoString) {
        this.geoString = geoString;
    }

    public String getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(String constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public Geometry getPlanGeo() {
        return planGeo;
    }

    public void setPlanGeo(Geometry planGeo) {
        this.planGeo = planGeo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getLandUse() {
        return landUse;
    }

    public void setLandUse(String landUse) {
        this.landUse = landUse;
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        this.landUseCode = landUseCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLandArea() {
        return landArea;
    }

    public void setLandArea(int landArea) {
        this.landArea = landArea;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
