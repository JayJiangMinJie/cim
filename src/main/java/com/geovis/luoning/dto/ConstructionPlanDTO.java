package com.geovis.luoning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.postgis.Geometry;
import org.postgis.Polygon;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 建设规划;
 * @author : jay
 * @date : 2022-9-8
 */
@ApiModel(value = "建设规划",description = "")
public class ConstructionPlanDTO implements Serializable,Cloneable{
    @ApiModelProperty(value = "规划id",notes = "规划id")
    private String planCode ;
    @ApiModelProperty(value = "土地用途",notes = "土地用途")
    private String landUse ;
    @ApiModelProperty(value = "土地用途编码",notes = "土地用途编码")
    private String landUseCode ;
    @ApiModelProperty(value = "地址",notes = "地址")
    private String address ;
    @ApiModelProperty(value = "建设状态",notes = "建设状态")
    private String constructionStatus ;
    @ApiModelProperty(value = "土地面积",notes = "土地面积")
    private Integer landArea ;
    @ApiModelProperty(value = "图片列表",notes = "图片列表")
    private List<MultipartFile> images ;
    @ApiModelProperty(value = "图片路径",notes = "图片路径")
    private String imagesPath ;
    @ApiModelProperty(value = "空间数据",notes = "空间数据")
    private String geoString;
    @ApiModelProperty(value = "空间数据",notes = "空间数据")
    private Polygon planGeo;

    public String getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(String constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public void setLandArea(Integer landArea) {
        this.landArea = landArea;
    }

    public Polygon getPlanGeo() {
        return planGeo;
    }

    public void setPlanGeo(Polygon planGeo) {
        this.planGeo = planGeo;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getGeoString() {
        return geoString;
    }

    public void setGeoString(String geoString) {
        this.geoString = geoString;
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

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}