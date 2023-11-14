package com.geovis.luoning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 建设规划VO;
 * @author : jay
 * @date : 2023-3-14
 */
@ApiModel(value = "建设规划VO",description = "")
public class ConstructionPlanResultVO implements Serializable,Cloneable{
    /** 建设规划列表*/
    @ApiModelProperty(name = "建设规划列表",notes = "建设规划列表")
    private List<ConstructionPlanVO> content;
    @ApiModelProperty(name = "具体建设规划总数",notes = "具体建设规划总数")
    private long totalElements ;

    public List<ConstructionPlanVO> getContent() {
        return content;
    }

    public void setContent(List<ConstructionPlanVO> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}