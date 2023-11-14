package com.geovis.luoning.service.business.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.bo.ConstructionPlanBO;
import com.geovis.luoning.converter.ConstructionPlanConverter;
import com.geovis.luoning.dto.ConstructionPlanDTO;
import com.geovis.luoning.mapper.ConstructionPlanMapper;
import com.geovis.luoning.po.ConstructionPlanPO;
import com.geovis.luoning.po.ConstructionPlanPO;
import com.geovis.luoning.service.business.ConstructionPlanService;
import com.geovis.luoning.utils.ExcelExportUtil;
import com.geovis.luoning.utils.FileTransUtils;
import com.geovis.luoning.utils.GeometryConversionUtils;
import com.geovis.luoning.vo.ConstructionPlanstatisticsVO;
import org.postgis.PGgeometry;
import org.postgis.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
* 建设规划表;(construction_plan)表服务实现类
* @author : jay
* @date : 2022-9-8
*/
@Service
public class ConstructionPlanServiceImpl extends ServiceImpl<ConstructionPlanMapper, ConstructionPlanPO> implements ConstructionPlanService {

    Logger log = Logger.getLogger(ConstructionPlanServiceImpl.class.getName());

    @Autowired
    private ConstructionPlanMapper constructionPlanMapper;

    @Autowired
    ExcelExportUtil exportUtils;

    @Value("${file.uploadPathCon}")
    String fileUpload;

    /**
     * 通过规划类型、状态导出excel数据
     *
     * @param searchContent 建设内容
     * @return 实例对象
     */
    @Override
    public String exportConstructionPlanExcel(String searchContent){
        String excelPath = null;
        try {
            excelPath = exportUtils.exportConstructionPlanDataToExcel(searchContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return excelPath;
    }

    /**
     * 获取规划统计数据
     *
     * @return 实例对象
     */
    @Override
    public ConstructionPlanstatisticsVO getConstructionPlanStatisticsData(){
        List<ConstructionPlanPO> constructionPlanPOS = constructionPlanMapper.selectConstructionPlan();
        if(constructionPlanPOS.size() == 0){
            return null;
        }
        Map<String, Double> constructionPlanAndRateMap = new HashMap<>();
        Map<String, Double> beforeMap = new HashMap<>();
        Map<String, Double> midMap = new HashMap<>();
        Map<String, Double> afterMap = new HashMap<>();
        Map<String, Map<String, Double>> constructionPlanLandMap = new HashMap<>();

        int listSize = constructionPlanPOS.size();

        int constructionLandUseNum = 0;
        int constructionLandUseBeforeNum = 0;
        int constructionLandUseMidNum = 0;
        int constructionLandUseAfterNum = 0;

        int residentialLandUseNum = 0;
        int residentialLandUseBeforeNum = 0;
        int residentialLandUseMidNum = 0;
        int residentialLandUseAfterNum = 0;

        int mineStorageNum = 0;
        int mineStorageBeforeNum = 0;
        int mineStorageMidNum = 0;
        int mineStorageAfterNum = 0;

        int commercialLandUseNum = 0;
        int commercialLandUseBeforeNum = 0;
        int commercialLandUseMidNum = 0;
        int commercialLandUseAfterNum = 0;

        int transportNum = 0;
        int transportBeforeNum = 0;
        int transportMidNum = 0;
        int transportAfterNum = 0;

        int specialLandUseNum = 0;
        int specialLandUseBeforeNum = 0;
        int specialLandUseMidNum = 0;
        int specialLandUseAfterNum = 0;

        int publicServiceNum = 0;
        int publicServiceBeforeNum = 0;
        int publicServiceMidNum = 0;
        int publicServiceAfterNum = 0;

        for(ConstructionPlanPO constructionPlanPO : constructionPlanPOS){
            String landUse = constructionPlanPO.getLandUseCode();
            //建筑用地
            if((landUse.startsWith("05") || landUse.startsWith("06") || landUse.startsWith("07") || landUse.startsWith("08") || landUse.startsWith("09") || landUse.startsWith("10")) && !landUse.equals("1006")){
                constructionLandUseNum = constructionLandUseNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    constructionLandUseMidNum = constructionLandUseMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    constructionLandUseBeforeNum = constructionLandUseBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    constructionLandUseAfterNum = constructionLandUseAfterNum + 1;
                }
            }
            //住宅用地
            if(landUse.startsWith("07")){
                residentialLandUseNum = residentialLandUseNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    residentialLandUseMidNum = residentialLandUseMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    residentialLandUseBeforeNum = residentialLandUseBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    residentialLandUseAfterNum = residentialLandUseAfterNum + 1;
                }
            }
            //工矿仓储
            if(landUse.startsWith("06")){
                mineStorageNum = mineStorageNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    mineStorageMidNum = mineStorageMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    mineStorageBeforeNum = mineStorageBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    mineStorageAfterNum = mineStorageAfterNum + 1;
                }
            }
            //商服用地
            if(landUse.startsWith("05")){
                commercialLandUseNum = commercialLandUseNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    commercialLandUseMidNum = commercialLandUseMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    commercialLandUseBeforeNum = commercialLandUseBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    commercialLandUseAfterNum = commercialLandUseAfterNum + 1;
                }
            }
            //交通运输
            if(landUse.startsWith("10")){
                transportNum = transportNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    transportMidNum = transportMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    transportBeforeNum = transportBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    transportAfterNum = transportAfterNum + 1;
                }
            }
            //特殊用地
            if(landUse.startsWith("09")){
                specialLandUseNum = specialLandUseNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    specialLandUseMidNum = specialLandUseMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    specialLandUseBeforeNum = specialLandUseBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    specialLandUseAfterNum = specialLandUseAfterNum + 1;
                }
            }
            //公共服务
            if(landUse.startsWith("08")){
                publicServiceNum = publicServiceNum + 1;
                if(("建设中").equals(constructionPlanPO.getConstructionStatus())){
                    publicServiceMidNum = publicServiceMidNum + 1;
                } else if(("未开工").equals(constructionPlanPO.getConstructionStatus())) {
                    publicServiceBeforeNum = publicServiceBeforeNum + 1;
                }else if(("已完工").equals(constructionPlanPO.getConstructionStatus())) {
                    publicServiceAfterNum = publicServiceAfterNum + 1;
                }
            }
        }
        constructionPlanAndRateMap.put("建筑用地数", (double) constructionLandUseNum);
        constructionPlanAndRateMap.put("建筑用地占比", (double) constructionLandUseNum/listSize);
        constructionPlanAndRateMap.put("住宅用地数", (double) residentialLandUseNum);
        constructionPlanAndRateMap.put("住宅用地占比", (double) residentialLandUseNum/listSize);
        constructionPlanAndRateMap.put("工矿仓储数", (double) mineStorageNum);
        constructionPlanAndRateMap.put("工矿仓储占比", (double) mineStorageNum/listSize);
        constructionPlanAndRateMap.put("商服用地数", (double) commercialLandUseNum);
        constructionPlanAndRateMap.put("商服用地占比", (double) commercialLandUseNum/listSize);
        constructionPlanAndRateMap.put("交通运输数", (double) transportNum);
        constructionPlanAndRateMap.put("交通运输占比", (double) transportNum/listSize);
        constructionPlanAndRateMap.put("特殊用地数", (double) specialLandUseNum);
        constructionPlanAndRateMap.put("特殊用地占比", (double) specialLandUseNum/listSize);
        constructionPlanAndRateMap.put("公共服务数", (double) publicServiceNum);
        constructionPlanAndRateMap.put("公共服务占比", (double) publicServiceNum/listSize);

        beforeMap.put("建筑用地", (double) constructionLandUseBeforeNum);
        beforeMap.put("住宅用地", (double)residentialLandUseBeforeNum);
        beforeMap.put("工矿仓储", (double)mineStorageBeforeNum);
        beforeMap.put("商服用地", (double)commercialLandUseBeforeNum);
        beforeMap.put("交通运输", (double)transportBeforeNum);
        beforeMap.put("公共服务", (double)publicServiceBeforeNum);
        beforeMap.put("特殊用地", (double)specialLandUseBeforeNum);

        midMap.put("建筑用地", (double)constructionLandUseMidNum);
        midMap.put("住宅用地", (double)residentialLandUseMidNum);
        midMap.put("工矿仓储", (double)mineStorageMidNum);
        midMap.put("商服用地", (double)commercialLandUseMidNum);
        midMap.put("交通运输", (double)transportMidNum);
        midMap.put("公共服务", (double)publicServiceMidNum);
        midMap.put("特殊用地", (double)specialLandUseMidNum);

        afterMap.put("建筑用地", (double)constructionLandUseAfterNum);
        afterMap.put("住宅用地", (double)residentialLandUseAfterNum);
        afterMap.put("工矿仓储", (double)mineStorageAfterNum);
        afterMap.put("商服用地", (double)commercialLandUseAfterNum);
        afterMap.put("交通运输", (double)transportAfterNum);
        afterMap.put("公共服务", (double)publicServiceAfterNum);
        afterMap.put("特殊用地", (double)specialLandUseAfterNum);

        constructionPlanLandMap.put("未开工", beforeMap);
        constructionPlanLandMap.put("建设中", midMap);
        constructionPlanLandMap.put("已完成", afterMap);

        ConstructionPlanstatisticsVO constructionPlanstatisticsVO = new ConstructionPlanstatisticsVO();
        constructionPlanstatisticsVO.setConstructionLandUseNum(constructionPlanAndRateMap.get("建筑用地数"));
        constructionPlanstatisticsVO.setConstructionLandUseRate(constructionPlanAndRateMap.get("建筑用地占比"));
        constructionPlanstatisticsVO.setResidentialLandUseNum(constructionPlanAndRateMap.get("住宅用地数"));
        constructionPlanstatisticsVO.setResidentialLandUseRate(constructionPlanAndRateMap.get("住宅用地占比"));
        constructionPlanstatisticsVO.setMineStorageNum(constructionPlanAndRateMap.get("工矿仓储数"));
        constructionPlanstatisticsVO.setMineStorageRate(constructionPlanAndRateMap.get("工矿仓储占比"));
        constructionPlanstatisticsVO.setCommercialLandUseNum(constructionPlanAndRateMap.get("商服用地数"));
        constructionPlanstatisticsVO.setCommercialLandUseRate(constructionPlanAndRateMap.get("商服用地占比"));
        constructionPlanstatisticsVO.setTransportNum(constructionPlanAndRateMap.get("交通运输数"));
        constructionPlanstatisticsVO.setTransportRate(constructionPlanAndRateMap.get("交通运输占比"));
        constructionPlanstatisticsVO.setSpecialLandUseNum(constructionPlanAndRateMap.get("特殊用地数"));
        constructionPlanstatisticsVO.setSpecialLandUseRate(constructionPlanAndRateMap.get("特殊用地占比"));
        constructionPlanstatisticsVO.setPublicServiceNum(constructionPlanAndRateMap.get("公共服务数"));
        constructionPlanstatisticsVO.setPublicServiceRate(constructionPlanAndRateMap.get("公共服务占比"));
        constructionPlanstatisticsVO.setConstructionPlanLandMap(constructionPlanLandMap);
        return constructionPlanstatisticsVO;
    }


    @Override
    public ApiResp addOrUpdateConstructionPlan(ConstructionPlanDTO constructionPlanDTO) {
        ApiResp apiResp = new ApiResp();
        ConstructionPlanPO constructionPlanPO = null;
        String getJson = constructionPlanDTO.getGeoString();
        String wkt = GeometryConversionUtils.getWkt(getJson);
        PGgeometry pgGeometry = null;
        try {
            pgGeometry = new PGgeometry(wkt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Polygon polygon = (Polygon)pgGeometry.getGeometry();
        constructionPlanDTO.setPlanGeo(polygon);
        //图片列表转换为图片在服务器的路径
        if(!StringUtils.isEmpty(constructionPlanDTO.getImages())){
            List<MultipartFile> images = constructionPlanDTO.getImages();
            String imagesPath = FileTransUtils.uploadFiles(images, fileUpload);
            constructionPlanDTO.setImagesPath(imagesPath);
        }
        //判断新增或者更新
        QueryWrapper<ConstructionPlanPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_code", constructionPlanDTO.getPlanCode());
        // 查询记录数量
        Integer count = constructionPlanMapper.selectCount(queryWrapper);
        constructionPlanPO = constructionPlanDTO2PO(constructionPlanDTO);
        if(count <= 0){
            int addResult = constructionPlanMapper.insertOne(constructionPlanPO);
            if(addResult > 0){
                apiResp.setStatus(200);
                apiResp.setMessage("新增成功");
            }else {
                apiResp.setStatus(201);
                apiResp.setMessage("新增失败");
            }
        }else{
            UpdateWrapper<ConstructionPlanPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("plan_code", constructionPlanDTO.getPlanCode());
            int updateResult = constructionPlanMapper.update(constructionPlanPO, updateWrapper);
            if(updateResult > 0){
                apiResp.setStatus(200);
                apiResp.setMessage("更新成功");
            }else {
                apiResp.setStatus(202);
                apiResp.setMessage("更新失败");
            }
        }
        return apiResp;
    }

    @Override
    public ApiResp daleteConstructionPlan(String planCode) {
        ApiResp apiResp = new ApiResp();
        QueryWrapper<ConstructionPlanPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_code", planCode);
        // 查询记录数量
        Integer count = constructionPlanMapper.selectCount(queryWrapper);
        if(count <= 0){
            apiResp.setStatus(203);
            apiResp.setMessage("数据不存在");
        }else{
            UpdateWrapper<ConstructionPlanPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("plan_code", planCode);
            int deleteResult = constructionPlanMapper.delete(updateWrapper);
            if(deleteResult > 0){
                apiResp.setStatus(200);
                apiResp.setMessage("删除成功");
            }else {
                apiResp.setStatus(202);
                apiResp.setMessage("删除失败");
            }
        }
        return apiResp;
    }


    @Override
    public ApiResp addConstructionPlanImages(String planCode, List<MultipartFile> images){
        ApiResp apiResp = new ApiResp();
        ConstructionPlanPO constructionPlanPO = null;
        QueryWrapper<ConstructionPlanPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_code", planCode);
        // 查询记录数量
        constructionPlanPO = constructionPlanMapper.selectOne(queryWrapper);
        if(constructionPlanPO  == null){
           throw new RuntimeException("数据不存在，新增异常");
        }else{
            //图片列表转换为图片在服务器的路径
            String imagesPath = FileTransUtils.uploadFiles(images, fileUpload);
            String oldImagePath = constructionPlanPO.getImagesPath();
            String newImagePath = oldImagePath + "," + imagesPath;
            constructionPlanPO.setImagesPath(newImagePath);
            UpdateWrapper<ConstructionPlanPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("plan_code", planCode);
            int addResult = constructionPlanMapper.update(constructionPlanPO, updateWrapper);
            if(addResult > 0){
                apiResp.setStatus(200);
                apiResp.setMessage("新增成功");
            }else {
                apiResp.setStatus(201);
                apiResp.setMessage("新增失败");
            }
        }
        return apiResp;
    }

    /**
    * 分页查询
    *
    * @param current 当前页码
    * @param size  每页大小
    * @return
    */
    @Override
    public Page<ConstructionPlanBO> paginQuery(String searchContent, long current, long size){
        //执行分页查询
        Page<ConstructionPlanPO> pagin = new Page<>(current , size , true);
        IPage<ConstructionPlanPO> selectResult = constructionPlanMapper.selectByPage(pagin, searchContent);
        pagin.setPages(selectResult.getPages());
        pagin.setTotal(selectResult.getTotal());
        pagin.setRecords(selectResult.getRecords());
        //返回结果
        return ConstructionPlanConverter.INSTANCE.toConstructionPlanBOPageByPO(pagin);
    }

    private ConstructionPlanPO constructionPlanDTO2PO(ConstructionPlanDTO dto){
        ConstructionPlanPO po = new ConstructionPlanPO();
        po.setConstructionStatus(dto.getConstructionStatus());
        po.setPlanCode(dto.getPlanCode());
        po.setPlanGeo(dto.getPlanGeo());
        po.setLandArea(dto.getLandArea());
        po.setAddress(dto.getAddress());
        po.setLandUse(dto.getLandUse());
        po.setLandUseCode(dto.getLandUseCode());
        po.setImagesPath(dto.getImagesPath());
        return po;
    }
}