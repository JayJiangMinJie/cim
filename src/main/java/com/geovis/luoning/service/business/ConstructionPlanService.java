package com.geovis.luoning.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.bo.ConstructionPlanBO;
import com.geovis.luoning.dto.ConstructionPlanDTO;
import com.geovis.luoning.vo.ConstructionPlanstatisticsVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (AlertDetails)表服务接口
 * @author : jay
 * @date : 2022-9-8
 */
public interface ConstructionPlanService {

    /**
     * 通过建设类型、状态导出excel数据
     *
     * @param searchContent 建设内容
     * @return 实例对象
     */
    String exportConstructionPlanExcel(String searchContent);

    /**
     * 获取建设统计数据
     *
     * @return 实例对象
     */
    ConstructionPlanstatisticsVO getConstructionPlanStatisticsData();

    /**
     * 新增/更新建设规划数据
     *
     * @param constructionPlanDTO 建设监测dto
     * @return 实例对象
     */
    ApiResp addOrUpdateConstructionPlan(ConstructionPlanDTO constructionPlanDTO);

    /**
     * 删除建设规划数据
     *
     * @param planCode 建设规划code
     * @return 实例对象
     */
    ApiResp daleteConstructionPlan(String planCode);

    /**
     * 新增建设规划照片
     *
     * @param planCode 数据唯一值
     * @param images 新增图片
     * @return 实例对象
     */
    ApiResp addConstructionPlanImages(String planCode, List<MultipartFile> images);

    /**
     * 分页查询
     *
     * @param current 当前页码
     * @param size  每页大小
     * @return
     */
    Page<ConstructionPlanBO> paginQuery(String searchContent, long current, long size);

}