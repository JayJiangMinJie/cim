package com.geovis.luoning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.dto.ConstructionPlanDTO;
import com.geovis.luoning.po.ConstructionPlanPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (construction_plan)表数据库访问层
 * @author : jay
 * @date : 2022-9-8
 */

@Mapper
public interface ConstructionPlanMapper extends BaseMapper<ConstructionPlanPO>{
     /**
      *
      * @return 插入一条数据
      */
     int insertOne(@Param("constructionPlanPO") ConstructionPlanPO constructionPlanPO);

     /**
      *
      * @return 查询单条数据
      */
     ConstructionPlanPO selectConstructionPlanById(@Param("id") int id);

     /**
      *
      * @return 查询所有数据
      */
     List<ConstructionPlanPO> selectConstructionPlan();

     /**
      *
      * @return 条件查询所有数据
      */
     List<ConstructionPlanPO> selectAllConstructionPlan(@Param("searchContent") String searchContent);

     /**
      * 分页查询指定行数据
      *
      * @param page 分页参数
      * @return 分页对象列表
      */
     Page<ConstructionPlanPO> selectByPage(Page<ConstructionPlanPO> page , @Param("searchContent") String searchContent);

}