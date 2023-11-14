package com.geovis.luoning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * 建设规划;
 * @author : jay
 * @date : 2023-3-14
 */
@ApiModel(value = "建设规划",description = "")
public class ConstructionPlanstatisticsVO implements Serializable,Cloneable{
    @ApiModelProperty(name = "建筑用地数",notes = "建筑用地数")
    private Double constructionLandUseNum;
    @ApiModelProperty(name = "建筑用地占比",notes = "建筑用地占比")
    private Double constructionLandUseRate;
    @ApiModelProperty(name = "住宅用地数",notes = "住宅用地数")
    private Double residentialLandUseNum;
    @ApiModelProperty(name = "住宅用地占比",notes = "住宅用地占比")
    private Double residentialLandUseRate;
    @ApiModelProperty(name = "工矿仓储数",notes = "工矿仓储数")
    private Double mineStorageNum;
    @ApiModelProperty(name = "工矿仓储占比",notes = "工矿仓储占比")
    private Double mineStorageRate;
    @ApiModelProperty(name = "商服用地数",notes = "商服用地数")
    private Double commercialLandUseNum;
    @ApiModelProperty(name = "商服用地占比",notes = "商服用地占比")
    private Double commercialLandUseRate;
    @ApiModelProperty(name = "交通运输数",notes = "交通运输数")
    private Double transportNum;
    @ApiModelProperty(name = "交通运输占比",notes = "交通运输占比")
    private Double transportRate;
    @ApiModelProperty(name = "特殊用地数",notes = "特殊用地数")
    private Double specialLandUseNum;
    @ApiModelProperty(name = "特殊用地占比",notes = "特殊用地占比")
    private Double specialLandUseRate;
    @ApiModelProperty(name = "公共服务数",notes = "公共服务数")
    private Double publicServiceNum;
    @ApiModelProperty(name = "公共服务占比",notes = "公共服务占比")
    private Double publicServiceRate;
    @ApiModelProperty(name = "土地规划建设",notes = "土地规划建设")
    private Map<String, Map<String, Double>> constructionPlanLandMap ;


    public Double getConstructionLandUseNum() {
        return constructionLandUseNum;
    }

    public void setConstructionLandUseNum(Double constructionLandUseNum) {
        this.constructionLandUseNum = constructionLandUseNum;
    }

    public Double getConstructionLandUseRate() {
        return constructionLandUseRate;
    }

    public void setConstructionLandUseRate(Double constructionLandUseRate) {
        this.constructionLandUseRate = constructionLandUseRate;
    }

    public Double getResidentialLandUseNum() {
        return residentialLandUseNum;
    }

    public void setResidentialLandUseNum(Double residentialLandUseNum) {
        this.residentialLandUseNum = residentialLandUseNum;
    }

    public Double getResidentialLandUseRate() {
        return residentialLandUseRate;
    }

    public void setResidentialLandUseRate(Double residentialLandUseRate) {
        this.residentialLandUseRate = residentialLandUseRate;
    }

    public Double getMineStorageNum() {
        return mineStorageNum;
    }

    public void setMineStorageNum(Double mineStorageNum) {
        this.mineStorageNum = mineStorageNum;
    }

    public Double getMineStorageRate() {
        return mineStorageRate;
    }

    public void setMineStorageRate(Double mineStorageRate) {
        this.mineStorageRate = mineStorageRate;
    }

    public Double getCommercialLandUseNum() {
        return commercialLandUseNum;
    }

    public void setCommercialLandUseNum(Double commercialLandUseNum) {
        this.commercialLandUseNum = commercialLandUseNum;
    }

    public Double getCommercialLandUseRate() {
        return commercialLandUseRate;
    }

    public void setCommercialLandUseRate(Double commercialLandUseRate) {
        this.commercialLandUseRate = commercialLandUseRate;
    }

    public Double getTransportNum() {
        return transportNum;
    }

    public void setTransportNum(Double transportNum) {
        this.transportNum = transportNum;
    }

    public Double getTransportRate() {
        return transportRate;
    }

    public void setTransportRate(Double transportRate) {
        this.transportRate = transportRate;
    }

    public Double getSpecialLandUseNum() {
        return specialLandUseNum;
    }

    public void setSpecialLandUseNum(Double specialLandUseNum) {
        this.specialLandUseNum = specialLandUseNum;
    }

    public Double getSpecialLandUseRate() {
        return specialLandUseRate;
    }

    public void setSpecialLandUseRate(Double specialLandUseRate) {
        this.specialLandUseRate = specialLandUseRate;
    }

    public Double getPublicServiceNum() {
        return publicServiceNum;
    }

    public void setPublicServiceNum(Double publicServiceNum) {
        this.publicServiceNum = publicServiceNum;
    }

    public Double getPublicServiceRate() {
        return publicServiceRate;
    }

    public void setPublicServiceRate(Double publicServiceRate) {
        this.publicServiceRate = publicServiceRate;
    }

    public Map<String, Map<String, Double>> getConstructionPlanLandMap() {
        return constructionPlanLandMap;
    }

    public void setConstructionPlanLandMap(Map<String, Map<String, Double>> constructionPlanLandMap) {
        this.constructionPlanLandMap = constructionPlanLandMap;
    }
}