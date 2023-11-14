package com.geovis.luoning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.dto.IllegalBuildDTO;
import com.geovis.luoning.po.IllegalBuildPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (illegal_build)表数据库访问层
 * @author : jay
 * @date : 2022-9-8
 */

@Mapper
public interface IllegalBuildMapper extends BaseMapper<IllegalBuildPO>{
     /**
      *
      * @return 插入一条数据
      */
//     int insertOne(@Param("illegalBuildDTO") IllegalBuildDTO illegalBuildDTO);

     /**
      *
      * @return 查询单条数据
      */
     IllegalBuildPO selectIllegalBuildById(@Param("id") int id);

     /**
      *
      * @return 查询所有数据
      */
     List<IllegalBuildPO> selectIllegalBuild();

     /**
      * 分页查询指定行数据
      *
      * @param page 分页参数
      * @return 分页对象列表
      */
     Page<IllegalBuildPO> selectByPage(Page<IllegalBuildPO> page , @Param("illegalBuildType") String illegalBuildType, @Param("illegalBuildStatus") String illegalBuildStatus);

}