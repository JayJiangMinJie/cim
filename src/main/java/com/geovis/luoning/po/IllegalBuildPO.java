package com.geovis.luoning.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 违建监测表;
 * @author : jay
 * @date : 2023-3-14
 */
@ApiModel(value = "违建监测表",description = "")
@TableName("illegal_build")
public class IllegalBuildPO implements Serializable,Cloneable{
    @TableId(type = IdType.AUTO)
    private int id;
    private String illegalBuildCode ;
    private String illegalBuildType ;
    private String illegalBuildStatus ;
    private String buildStaff ;
    private String address ;
    private String buildArea ;
    private String demolishedArea ;
    private LocalDateTime supervisionDate ;
    private String responsiblePerson ;
    private String currentDesc ;
    private String recommendedMeasure ;
    private LocalDateTime enforcementStartDate ;
    private LocalDateTime enforcementEndDate ;
    private String progress ;
    private String photoPeriod ;
    private String beforeImagesPath;
    private String midImagesPath;
    private String afterImagesPath;
    private double longitude ;
    private double latitude ;
    private int height ;
    private LocalDateTime createTime;
    private String townStreet;

    public String getTownStreet() {
        return townStreet;
    }

    public void setTownStreet(String townStreet) {
        this.townStreet = townStreet;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIllegalBuildCode() {
        return illegalBuildCode;
    }

    public void setIllegalBuildCode(String illegalBuildCode) {
        this.illegalBuildCode = illegalBuildCode;
    }

    public String getIllegalBuildType() {
        return illegalBuildType;
    }

    public void setIllegalBuildType(String illegalBuildType) {
        this.illegalBuildType = illegalBuildType;
    }

    public String getIllegalBuildStatus() {
        return illegalBuildStatus;
    }

    public void setIllegalBuildStatus(String illegalBuildStatus) {
        this.illegalBuildStatus = illegalBuildStatus;
    }

    public String getBuildStaff() {
        return buildStaff;
    }

    public void setBuildStaff(String buildStaff) {
        this.buildStaff = buildStaff;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(String buildArea) {
        this.buildArea = buildArea;
    }

    public String getDemolishedArea() {
        return demolishedArea;
    }

    public void setDemolishedArea(String demolishedArea) {
        this.demolishedArea = demolishedArea;
    }

    public LocalDateTime getSupervisionDate() {
        return supervisionDate;
    }

    public void setSupervisionDate(LocalDateTime supervisionDate) {
        this.supervisionDate = supervisionDate;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getCurrentDesc() {
        return currentDesc;
    }

    public void setCurrentDesc(String currentDesc) {
        this.currentDesc = currentDesc;
    }

    public String getRecommendedMeasure() {
        return recommendedMeasure;
    }

    public void setRecommendedMeasure(String recommendedMeasure) {
        this.recommendedMeasure = recommendedMeasure;
    }

    public LocalDateTime getEnforcementStartDate() {
        return enforcementStartDate;
    }

    public void setEnforcementStartDate(LocalDateTime enforcementStartDate) {
        this.enforcementStartDate = enforcementStartDate;
    }

    public LocalDateTime getEnforcementEndDate() {
        return enforcementEndDate;
    }

    public void setEnforcementEndDate(LocalDateTime enforcementEndDate) {
        this.enforcementEndDate = enforcementEndDate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPhotoPeriod() {
        return photoPeriod;
    }

    public void setPhotoPeriod(String photoPeriod) {
        this.photoPeriod = photoPeriod;
    }

    public String getBeforeImagesPath() {
        return beforeImagesPath;
    }

    public void setBeforeImagesPath(String beforeImagesPath) {
        this.beforeImagesPath = beforeImagesPath;
    }

    public String getMidImagesPath() {
        return midImagesPath;
    }

    public void setMidImagesPath(String midImagesPath) {
        this.midImagesPath = midImagesPath;
    }

    public String getAfterImagesPath() {
        return afterImagesPath;
    }

    public void setAfterImagesPath(String afterImagesPath) {
        this.afterImagesPath = afterImagesPath;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}