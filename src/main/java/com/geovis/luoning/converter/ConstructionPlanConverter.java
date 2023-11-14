package com.geovis.luoning.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.bo.ConstructionPlanBO;
import com.geovis.luoning.po.ConstructionPlanPO;
import com.geovis.luoning.vo.ConstructionPlanVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author jay
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ConstructionPlanConverter {

    /**
     * 类转换器实例
     */
    ConstructionPlanConverter INSTANCE = Mappers.getMapper(ConstructionPlanConverter.class);


    /**
     * BO -> VO
     */
    ConstructionPlanVO toConstructionPlanVOByBO(ConstructionPlanBO bo);

    /**
     * PO -> BO
     */
    ConstructionPlanBO toConstructionPlanBOByPO(ConstructionPlanPO po);

    /**
     * PO(page) -> BO(page)
     */
    Page<ConstructionPlanBO> toConstructionPlanBOPageByPO(Page<ConstructionPlanPO> poPage);

    /**
     * BO(page) -> VO(page)
     */
    Page<ConstructionPlanVO> toConstructionPlanVOPageByBO(Page<ConstructionPlanBO> poPage);



}
