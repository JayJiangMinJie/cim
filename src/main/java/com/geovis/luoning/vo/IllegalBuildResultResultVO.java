package com.geovis.luoning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 违法建筑VO;
 * @author : jay
 * @date : 2023-3-14
 */
@ApiModel(value = "违法建筑VO",description = "")
public class IllegalBuildResultResultVO implements Serializable,Cloneable{
    /** 违法建筑列表*/
    @ApiModelProperty(name = "违法建筑列表",notes = "违法建筑列表")
    private List<IllegalBuildVO> content;
    @ApiModelProperty(name = "具体违法建筑总数",notes = "具体违法建筑总数")
    private long totalElements ;

    public List<IllegalBuildVO> getContent() {
        return content;
    }

    public void setContent(List<IllegalBuildVO> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}