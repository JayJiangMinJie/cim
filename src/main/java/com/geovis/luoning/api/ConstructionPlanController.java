package com.geovis.luoning.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.bo.ConstructionPlanBO;
import com.geovis.luoning.converter.ConstructionPlanConverter;
import com.geovis.luoning.dto.ConstructionPlanDTO;
import com.geovis.luoning.service.business.ConstructionPlanService;
import com.geovis.luoning.vo.ConstructionPlanResultVO;
import com.geovis.luoning.vo.ConstructionPlanVO;
import com.geovis.luoning.vo.ConstructionPlanstatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* 建设规划;(construction_plan)控制层
* @author : jay
* @date : 2023-9-19
*/
@Api(value = "ConstructionPlanController", tags = "建设规划功能接口")
@RestController
@RequestMapping("/ConstructionPlan")
public class ConstructionPlanController {
   @Autowired
   private ConstructionPlanService constructionPlanService;

    /**
     * 通过建设类型、状态导出excel数据
     *
     * @param searchContent 建设类型
     * @return 实例对象
     */
    @ApiOperation("通过建设类型、状态导出excel数据")
    @GetMapping("getConstructionPlanExcelPath")
    public ResponseEntity<String> getConstructionPlanExcelPath(@RequestParam(value = "searchContent", required = false) String searchContent){
        String excelPath = constructionPlanService.exportConstructionPlanExcel(searchContent);
        return ResponseEntity.ok(excelPath);
    }


    /**
     * 新增/更新建设规划数据
     *
     * @param ConstructionPlanDTO 建设监测dto
     * @return 实例对象
     */
    @ApiOperation("新增/更新建设规划数据")
    @PostMapping("addOrUpdateConstructionPlan")
    public ResponseEntity<ApiResp> addOrUpdateConstructionPlan(ConstructionPlanDTO ConstructionPlanDTO){
        ApiResp apiResp = constructionPlanService.addOrUpdateConstructionPlan(ConstructionPlanDTO);
        return ResponseEntity.ok(apiResp);
    }

    /**
     * 新增建设规划照片
     *
     * @param planCode 数据唯一值
     * @param images 新增图片
     * @return 实例对象
     */
    @ApiOperation("新增建设规划照片")
    @PostMapping("addConstructionPlanImages")
    public ResponseEntity<ApiResp> addConstructionPlanImages(@RequestParam(value = "planCode") String planCode,
                                                              @RequestParam(value = "images") List<MultipartFile> images){
        ApiResp apiResp = constructionPlanService.addConstructionPlanImages(planCode, images);
        return ResponseEntity.ok(apiResp);
    }

    /**
     * 获取建设统计数据
     *
     * @return 实例对象
     */
    @ApiOperation("获取建设统计数据")
    @GetMapping("getConstructionPlanStatisticsData")
    public ResponseEntity<ConstructionPlanstatisticsVO> getConstructionPlanStatisticsData(){
        ConstructionPlanstatisticsVO ConstructionPlanstatisticsVO = constructionPlanService.getConstructionPlanStatisticsData();
        return ResponseEntity.ok(ConstructionPlanstatisticsVO);
    }

    /**
     * 删除建设规划数据
     *
     * @param planCode 建设规划code
     * @return 实例对象
     */
    @ApiOperation("删除建设规划数据")
    @PostMapping("daleteConstructionPlan")
    public ResponseEntity<ApiResp> daleteConstructionPlan(@RequestParam(value = "planCode") String planCode){
        ApiResp apiResp = constructionPlanService.daleteConstructionPlan(planCode);
        return ResponseEntity.ok(apiResp);
    }

   /**
    * 分页查询
    *
    * @return 查询结果
    */
   @ApiOperation("分页查询")
   @GetMapping("paginQuery")
   public ResponseEntity<ConstructionPlanResultVO> paginQuery(@RequestParam(value = "searchContent", required = false) String searchContent,
                                                                  @RequestParam(value = "pageNo") int pageNo,
                                                                  @RequestParam(value = "pageSize") int pageSize
   ){
       //1.分页参数
       Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
       PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
       //2.分页查询
       /*把Mybatis的分页对象做封装转换，MP的分页对象上有一些SQL敏感信息，还是通过spring的分页模型来封装数据吧*/
       Page<ConstructionPlanBO> ConstructionPlanBOPage = constructionPlanService.paginQuery(searchContent, pageNo, pageSize);
       Page<ConstructionPlanVO> pageResult = ConstructionPlanConverter.INSTANCE.toConstructionPlanVOPageByBO(ConstructionPlanBOPage);
       //3. 分页结果组装
       List<ConstructionPlanVO> dataList = pageResult.getRecords();
       for(ConstructionPlanVO constructionPlanVO : dataList){
           String geoString = constructionPlanVO.getPlanGeo().toString();
           constructionPlanVO.setGeoString(geoString);
           constructionPlanVO.setPlanGeo(null);
       }
       long total = pageResult.getTotal();
//       PageImpl<ConstructionPlanVO> retPage = new PageImpl<ConstructionPlanVO>(dataList,pageRequest,total);
       ConstructionPlanResultVO constructionPlanVO = new ConstructionPlanResultVO();
       constructionPlanVO.setContent(dataList);
       constructionPlanVO.setTotalElements(total);
       return ResponseEntity.ok(constructionPlanVO);
   }

}