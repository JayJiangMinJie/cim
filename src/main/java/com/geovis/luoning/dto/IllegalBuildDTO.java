package com.geovis.luoning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 违建监察;
 * @author : jay
 * @date : 2022-9-8
 */
@ApiModel(value = "违建监察",description = "")
public class IllegalBuildDTO implements Serializable,Cloneable{
    @ApiModelProperty(value = "违建Code",notes = "违建Code", required = true)
    private String illegalBuildCode ;
    @ApiModelProperty(value = "违建类型",notes = "违建类型", required = true)
    private String illegalBuildType ;
    @ApiModelProperty(value = "违建状态",notes = "违建状态")
    private String illegalBuildStatus ;
    @ApiModelProperty(value = "建设人",notes = "建设人")
    private String buildStaff ;
    @ApiModelProperty(value = "详细地址",notes = "详细地址")
    private String address ;
    @ApiModelProperty(value = "建设面积",notes = "建设面积")
    private String buildArea ;
    @ApiModelProperty(value = "需拆除面积",notes = "需拆除面积")
    private String demolishedArea ;
    @ApiModelProperty(value = "监察日期",notes = "监察日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime supervisionDate ;
    @ApiModelProperty(value = "责任人",notes = "责任人")
    private String responsiblePerson ;
    @ApiModelProperty(value = "现状描述",notes = "现状描述")
    private String currentDesc ;
    @ApiModelProperty(value = "建议措施",notes = "建议措施")
    private String recommendedMeasure ;
    @ApiModelProperty(value = "执法开始日期",notes = "执法开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enforcementStartDate ;
    @ApiModelProperty(value = "执法结束日期",notes = "执法结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enforcementEndDate ;
    @ApiModelProperty(value = "进展情况",notes = "进展情况")
    private String progress ;
    @ApiModelProperty(value = "照片时段",notes = "照片时段")
    private String photoPeriod ;
    @ApiModelProperty(value = "执法前图片列表",notes = "执法前图片列表")
    private List<MultipartFile> beforeImages ;
    @ApiModelProperty(value = "执法中图片列表",notes = "执法中图片列表")
    private List<MultipartFile> midImages ;
    @ApiModelProperty(value = "执法后图片列表",notes = "执法后图片列表")
    private List<MultipartFile> afterImages ;
    @ApiModelProperty(value = "执法前图片路径",notes = "执法前图片路径")
    private String beforeImagesPath ;
    @ApiModelProperty(value = "执法中图片路径",notes = "执法中图片路径")
    private String midImagesPath ;
    @ApiModelProperty(value = "执法后图片路径",notes = "执法后图片路径")
    private String afterImagesPath ;
    @ApiModelProperty(value = "经度",notes = "经度", required = true)
    private double longitude ;
    @ApiModelProperty(value = "纬度",notes = "纬度", required = true)
    private double latitude ;
    @ApiModelProperty(value = "高度",notes = "高度", required = true)
    private int height ;
    @ApiModelProperty(value = "乡镇街道",notes = "乡镇街道")
    private String townStreet ;

    public String getTownStreet() {
        return townStreet;
    }

    public void setTownStreet(String townStreet) {
        this.townStreet = townStreet;
    }

    public String getIllegalBuildCode() {
        return illegalBuildCode;
    }

    public void setIllegalBuildCode(String illegalBuildCode) {
        this.illegalBuildCode = illegalBuildCode;
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

    public List<MultipartFile> getBeforeImages() {
        return beforeImages;
    }

    public void setBeforeImages(List<MultipartFile> beforeImages) {
        this.beforeImages = beforeImages;
    }

    public List<MultipartFile> getMidImages() {
        return midImages;
    }

    public void setMidImages(List<MultipartFile> midImages) {
        this.midImages = midImages;
    }

    public List<MultipartFile> getAfterImages() {
        return afterImages;
    }

    public void setAfterImages(List<MultipartFile> afterImages) {
        this.afterImages = afterImages;
    }
}