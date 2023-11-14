package com.geovis.luoning.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.bo.IllegalBuildBO;
import com.geovis.luoning.po.IllegalBuildPO;
import com.geovis.luoning.vo.IllegalBuildVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author jay
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IllegalBuildConverter {

    /**
     * 类转换器实例
     */
    IllegalBuildConverter INSTANCE = Mappers.getMapper(IllegalBuildConverter.class);


    /**
     * BO -> VO
     */
    IllegalBuildVO toIllegalBuildVOByBO(IllegalBuildBO bo);

    /**
     * PO -> BO
     */
    IllegalBuildBO toIllegalBuildBOByPO(IllegalBuildPO po);

    /**
     * PO(page) -> BO(page)
     */
    Page<IllegalBuildBO> toIllegalBuildBOPageByPO(Page<IllegalBuildPO> poPage);

    /**
     * BO(page) -> VO(page)
     */
    Page<IllegalBuildVO> toIllegalBuildVOPageByBO(Page<IllegalBuildBO> poPage);



}
