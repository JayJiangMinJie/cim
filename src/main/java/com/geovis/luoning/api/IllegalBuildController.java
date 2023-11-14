package com.geovis.luoning.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.bo.IllegalBuildBO;
import com.geovis.luoning.converter.IllegalBuildConverter;
import com.geovis.luoning.dto.IllegalBuildDTO;
import com.geovis.luoning.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.geovis.luoning.service.business.IllegalBuildService;

import java.util.List;

/**
* 违建监管;(illegal_build)控制层
* @author : jay
* @date : 2023-9-19
*/
@Api(value = "IllegalBuildController", tags = "违建监管功能接口")
@RestController
@RequestMapping("/IllegalBuild")
public class IllegalBuildController {
   @Autowired
   private IllegalBuildService illegalBuildService;

    /**
     * 通过违建类型、状态导出excel数据
     *
     * @param illegalBuildType 违建类型
     * @param illegalBuildStatus 违建状态
     * @return 实例对象
     */
    @ApiOperation("通过违建类型、状态导出excel数据")
    @GetMapping("getIllegalBuildExcelPath")
    public ResponseEntity<String> getIllegalBuildExcelPath(@RequestParam(value = "illegalBuildType", required = false) String illegalBuildType,
                                                           @RequestParam(value = "illegalBuildStatus", required = false) String illegalBuildStatus){
        String excelPath = illegalBuildService.exportIllegalBuildExcel(illegalBuildType, illegalBuildStatus);
        return ResponseEntity.ok(excelPath);
    }


    /**
     * 新增/更新违建监管数据
     *
     * @param illegalBuildDTO 违建监测dto
     * @return 实例对象
     */
    @ApiOperation("新增/更新违建监管数据")
    @PostMapping("addOrUpdateIllegalBuild")
    public ResponseEntity<ApiResp> addOrUpdateIllegalBuild(IllegalBuildDTO illegalBuildDTO){
        ApiResp apiResp = illegalBuildService.addOrUpdateIllegalBuild(illegalBuildDTO);
        return ResponseEntity.ok(apiResp);
    }

    /**
     * 删除违建监管数据
     *
     * @param illegalBuildCode 违建监测code
     * @return 实例对象
     */
    @ApiOperation("删除违建监管数据")
    @PostMapping("daleteIllegalBuild")
    public ResponseEntity<ApiResp> daleteIllegalBuild(@RequestParam(value = "illegalBuildCode") String illegalBuildCode){
        ApiResp apiResp = illegalBuildService.daleteIllegalBuild(illegalBuildCode);
        return ResponseEntity.ok(apiResp);
    }

    /**
     * 获取违建统计数据
     *
     * @return 实例对象
     */
    @ApiOperation("获取违建统计数据")
    @GetMapping("getIllegalBuildStatisticsData")
    public ResponseEntity<IllegalBuildstatisticsVO> getIllegalBuildStatisticsData(){
        IllegalBuildstatisticsVO illegalBuildstatisticsVO = illegalBuildService.getIllegalBuildStatisticsData();
        return ResponseEntity.ok(illegalBuildstatisticsVO);
    }

   /**
    * 分页查询
    *
    * @return 查询结果
    */
   @ApiOperation("分页查询")
   @GetMapping("paginQuery")
   public ResponseEntity<IllegalBuildResultResultVO> paginQuery(@RequestParam(value = "illegalBuildType", required = false) String illegalBuildType,
                                                                    @RequestParam(value = "illegalBuildStatus", required = false) String illegalBuildStatus,
                                                                    @RequestParam(value = "pageNo") int pageNo,
                                                                    @RequestParam(value = "pageSize") int pageSize
   ){
       //1.分页参数
       Sort sort = Sort.by(Sort.Direction.ASC, "create_time");
       PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
       //2.分页查询
       /*把Mybatis的分页对象做封装转换，MP的分页对象上有一些SQL敏感信息，还是通过spring的分页模型来封装数据吧*/
       Page<IllegalBuildBO> IllegalBuildBOPage = illegalBuildService.paginQuery(illegalBuildType, illegalBuildStatus, pageNo, pageSize);
       Page<IllegalBuildVO> pageResult = IllegalBuildConverter.INSTANCE.toIllegalBuildVOPageByBO(IllegalBuildBOPage);
       //3. 分页结果组装
       List<IllegalBuildVO> dataList = pageResult.getRecords();
       long total = pageResult.getTotal();
//       PageImpl<IllegalBuildVO> retPage = new PageImpl<IllegalBuildVO>(dataList,pageRequest,total);
       IllegalBuildResultResultVO illegalBuildResultResultVO = new IllegalBuildResultResultVO();
       illegalBuildResultResultVO.setContent(dataList);
       illegalBuildResultResultVO.setTotalElements(total);
       return ResponseEntity.ok(illegalBuildResultResultVO);
   }

}