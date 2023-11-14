package com.geovis.luoning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 违建监测;
 * @author : jay
 * @date : 2023-3-14
 */
@ApiModel(value = "违建监测",description = "")
public class IllegalBuildstatisticsVO implements Serializable,Cloneable{
    @ApiModelProperty(name = "总任务量",notes = "总任务量")
    private Double taskSum ;
    @ApiModelProperty(name = "今日完成",notes = "今日完成")
    private Double finishedToday ;
    @ApiModelProperty(name = "累计完成",notes = "累计完成")
    private Double finishedSum ;
    @ApiModelProperty(name = "完成率",notes = "完成率")
    private Double finishedRate ;
    @ApiModelProperty(name = "违建分布",notes = "违建分布")
    private Map<String, Map<String, Double>> illegalBuildDistributionMap ;
    @ApiModelProperty(name = "违建面积",notes = "违建面积")
    private Map<String, Map<String, Double>> illegalBuildAreaMap ;
    @ApiModelProperty(name = "违建状态",notes = "违建状态")
    private Map<String, Integer> illegalBuildStatusMap;

    public Double getTaskSum() {
        return taskSum;
    }

    public void setTaskSum(Double taskSum) {
        this.taskSum = taskSum;
    }

    public Double getFinishedToday() {
        return finishedToday;
    }

    public void setFinishedToday(Double finishedToday) {
        this.finishedToday = finishedToday;
    }

    public Double getFinishedSum() {
        return finishedSum;
    }

    public void setFinishedSum(Double finishedSum) {
        this.finishedSum = finishedSum;
    }

    public Double getFinishedRate() {
        return finishedRate;
    }

    public void setFinishedRate(Double finishedRate) {
        this.finishedRate = finishedRate;
    }

    public Map<String, Map<String, Double>> getIllegalBuildDistributionMap() {
        return illegalBuildDistributionMap;
    }

    public void setIllegalBuildDistributionMap(Map<String, Map<String, Double>> illegalBuildDistributionMap) {
        this.illegalBuildDistributionMap = illegalBuildDistributionMap;
    }

    public Map<String, Map<String, Double>> getIllegalBuildAreaMap() {
        return illegalBuildAreaMap;
    }

    public void setIllegalBuildAreaMap(Map<String, Map<String, Double>> illegalBuildAreaMap) {
        this.illegalBuildAreaMap = illegalBuildAreaMap;
    }

    public Map<String, Integer> getIllegalBuildStatusMap() {
        return illegalBuildStatusMap;
    }

    public void setIllegalBuildStatusMap(Map<String, Integer> illegalBuildStatusMap) {
        this.illegalBuildStatusMap = illegalBuildStatusMap;
    }

}