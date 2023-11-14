package com.geovis.luoning.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.dto.IllegalBuildDTO;
import com.geovis.luoning.bo.IllegalBuildBO;
import com.geovis.luoning.vo.IllegalBuildstatisticsVO;

/**
 * (AlertDetails)表服务接口
 * @author : jay
 * @date : 2022-9-8
 */
public interface IllegalBuildService {

    /**
     * 通过违建类型、状态导出excel数据
     *
     * @param illegalBuildType 违建类型
     * @param illegalBuildStatus 违建状态
     * @return 实例对象
     */
    String exportIllegalBuildExcel(String illegalBuildType, String illegalBuildStatus);

    /**
     * 获取违建统计数据
     *
     * @return 实例对象
     */
    IllegalBuildstatisticsVO getIllegalBuildStatisticsData();

    /**
     * 新增/更新违建监管数据
     *
     * @param illegalBuildDTO 违建监测dto
     * @return 实例对象
     */
    ApiResp addOrUpdateIllegalBuild(IllegalBuildDTO illegalBuildDTO);


    /**
     * 删除违建监管数据
     *
     * @param illegalBuildCode 违建监测code
     * @return 实例对象
     */
    ApiResp daleteIllegalBuild(String illegalBuildCode);

    /**
     * 分页查询
     *
     * @param current 当前页码
     * @param size  每页大小
     * @return
     */
    Page<IllegalBuildBO> paginQuery(String illegalBuildType, String illegalBuildStatus, long current, long size);

}