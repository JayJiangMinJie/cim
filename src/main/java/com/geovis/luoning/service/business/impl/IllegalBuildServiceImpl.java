package com.geovis.luoning.service.business.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.geovis.luoning.base.ApiResp;
import com.geovis.luoning.converter.IllegalBuildConverter;
import com.geovis.luoning.dto.IllegalBuildDTO;
import com.geovis.luoning.bo.IllegalBuildBO;
import com.geovis.luoning.mapper.*;
import com.geovis.luoning.po.IllegalBuildPO;
import com.geovis.luoning.service.business.IllegalBuildService;
import com.geovis.luoning.utils.ExcelExportUtil;
import com.geovis.luoning.utils.FileTransUtils;
import com.geovis.luoning.vo.IllegalBuildstatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
* 违规建筑表;(illegal_build)表服务实现类
* @author : jay
* @date : 2022-9-8
*/
@Service
public class IllegalBuildServiceImpl extends ServiceImpl<IllegalBuildMapper, IllegalBuildPO> implements IllegalBuildService {

    Logger log = Logger.getLogger(IllegalBuildServiceImpl.class.getName());

    @Value("${file.uploadPathIll}")
    String fileUpload;

    @Autowired
    private IllegalBuildMapper illegalBuildMapper;

    @Autowired
    ExcelExportUtil exportUtils;

    /**
     * 通过违建类型、状态导出excel数据
     *
     * @param illegalBuildType 违建类型
     * @param illegalBuildStatus 违建状态
     * @return 实例对象
     */
    @Override
    public String exportIllegalBuildExcel(String illegalBuildType, String illegalBuildStatus){
        String excelPath = null;
        try {
            excelPath = exportUtils.exportIllegalBuildDataToExcel(illegalBuildType, illegalBuildStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return excelPath;
    }

    /**
     * 获取违建统计数据
     *
     * @return 实例对象
     */
    @Override
    public IllegalBuildstatisticsVO getIllegalBuildStatisticsData(){
        List<IllegalBuildPO> illegalBuildPOS = illegalBuildMapper.selectIllegalBuild();
        if(illegalBuildPOS.size() == 0){
            return null;
        }
        //1、违建分布
        Map<String, Map<String, Double>> illegalBuildDistributionMap = new HashMap<>();
        Map<String, Double> illegalBuildYongNingMap = new HashMap<>();
        Map<String, Double> illegalBuildChengJiaoMap = new HashMap<>();
        Map<String, Double> illegalBuildHuiZuMap = new HashMap<>();
        //1.1、扩建、加建、超占
        int yongNingExtensionNum = 0;
        int yongNingAddConNum = 0;
        int yongNingSuperNum = 0;
        int chengJiaoExtensionNum = 0;
        int chengJiaoAddConNum = 0;
        int chengJiaoSuperNum = 0;
        int huiZuExtensionNum = 0;
        int huiZuAddConNum = 0;
        int huiZuSuperNum = 0;
        //2、违建面积
        Map<String, Map<String, Double>> illegalBuildAreaMap = new HashMap<>();
        Map<String, Double> illegalBuildYongNingMonthMap = new HashMap<>();
        Map<String, Double> illegalBuildChengJiaoMonthMap = new HashMap<>();
        Map<String, Double> illegalBuildHuiZuMonthMap = new HashMap<>();
        //2.1、一年12个月数量
        int yongNingJanuaryNum = 0;
        int yongNingFebruaryNum = 0;
        int yongNingMarchNum = 0;
        int yongNingSprilNum = 0;
        int yongNingMayNum = 0;
        int yongNingJuneNum = 0;
        int yongNingJulyNum = 0;
        int yongNingAugustNum = 0;
        int yongNingSeptemberNum = 0;
        int yongNingOctoberNum = 0;
        int yongNingNovemberNum = 0;
        int yongNingDecemberNum = 0;
        //-----------------
        int chengJiaoJanuaryNum = 0;
        int chengJiaoFebruaryNum = 0;
        int chengJiaoMarchNum = 0;
        int chengJiaoSprilNum = 0;
        int chengJiaoMayNum = 0;
        int chengJiaoJuneNum = 0;
        int chengJiaoJulyNum = 0;
        int chengJiaoAugustNum = 0;
        int chengJiaoSeptemberNum = 0;
        int chengJiaoOctoberNum = 0;
        int chengJiaoNovemberNum = 0;
        int chengJiaoDecemberNum = 0;
        //------------------
        int huiZuJanuaryNum = 0;
        int huiZuFebruaryNum = 0;
        int huiZuMarchNum = 0;
        int huiZuSprilNum = 0;
        int huiZuMayNum = 0;
        int huiZuJuneNum = 0;
        int huiZuJulyNum = 0;
        int huiZuAugustNum = 0;
        int huiZuSeptemberNum = 0;
        int huiZuOctoberNum = 0;
        int huiZuNovemberNum = 0;
        int huiZuDecemberNum = 0;
        //3、违建状态
        Map<String, Integer> illegalBuildStatusMap = new HashMap<>();
        //3.1、未开始、进行中、已完成
        int beforeNum = 0;
        int midNum = 0;
        int afterNum = 0;
        // 获取当前日期
        LocalDateTime currentDateTime = LocalDateTime.now();
        //总任务数、今日完成数、累计完成数、完成率
        int taskSumNum = illegalBuildPOS.size();
        int finishedTodayNum = 0;
        int finishedSumNum = 0;
        double  finishedRateNum = 0;
        for(IllegalBuildPO illegalBuildPO : illegalBuildPOS){
            //违建统计
            if("已完成".equals(illegalBuildPO.getIllegalBuildStatus())){
                finishedSumNum = finishedSumNum + 1;
                afterNum = afterNum + 1;
                //比较是否是今日
                LocalDate currentDate = currentDateTime.toLocalDate();
                LocalDate dataDate = illegalBuildPO.getSupervisionDate().toLocalDate();
                if(currentDate.equals(dataDate)){
                    finishedTodayNum = finishedTodayNum + 1;
                }
            }else if ("未开始".equals(illegalBuildPO.getIllegalBuildStatus())){
                beforeNum = beforeNum + 1;
            }else if ("进行中".equals(illegalBuildPO.getIllegalBuildStatus())){
                midNum = midNum + 1;
            }
            //违建分布统计
            if("永宁街道".equals(illegalBuildPO.getTownStreet())){
                if("扩建".equals(illegalBuildPO.getIllegalBuildType())){
                    yongNingExtensionNum = yongNingExtensionNum + 1;
                }else if("加建".equals(illegalBuildPO.getIllegalBuildType())){
                    yongNingAddConNum = yongNingAddConNum + 1;
                }else if("超占".equals(illegalBuildPO.getIllegalBuildType())){
                    yongNingSuperNum = yongNingSuperNum + 1;
                }
                Month month = illegalBuildPO.getSupervisionDate().getMonth();
                int monthValue = month.getValue();
                if(monthValue == 1){
                    yongNingJanuaryNum = yongNingJanuaryNum + 1;
                }else if (monthValue == 2) {
                    yongNingFebruaryNum = yongNingFebruaryNum + 1;
                }else if (monthValue == 3) {
                    yongNingMarchNum = yongNingMarchNum + 1;
                }else if (monthValue == 4) {
                    yongNingSprilNum = yongNingSprilNum + 1;
                }else if (monthValue == 5) {
                    yongNingMayNum = yongNingMayNum + 1;
                }else if (monthValue == 6) {
                    yongNingJuneNum = yongNingJuneNum + 1;
                }else if (monthValue == 7) {
                    yongNingJulyNum = yongNingJulyNum + 1;
                }else if (monthValue == 8) {
                    yongNingAugustNum = yongNingAugustNum + 1;
                }else if (monthValue == 9) {
                    yongNingSeptemberNum = yongNingSeptemberNum + 1;
                }else if (monthValue == 10){
                    yongNingOctoberNum = yongNingOctoberNum + 1;
                }else if (monthValue == 11){
                    yongNingNovemberNum = yongNingNovemberNum + 1;
                }else if (monthValue == 12){
                    yongNingDecemberNum = yongNingDecemberNum + 1;
                }
            }
            if("城郊乡".equals(illegalBuildPO.getTownStreet())){
                if("扩建".equals(illegalBuildPO.getIllegalBuildType())){
                    chengJiaoExtensionNum = chengJiaoExtensionNum + 1;
                }else if("加建".equals(illegalBuildPO.getIllegalBuildType())){
                    chengJiaoAddConNum = chengJiaoAddConNum + 1;
                }else if("超占".equals(illegalBuildPO.getIllegalBuildType())){
                    chengJiaoSuperNum = chengJiaoSuperNum + 1;
                }
                Month month = illegalBuildPO.getSupervisionDate().getMonth();
                int monthValue = month.getValue();
                if(monthValue == 1){
                    chengJiaoJanuaryNum = chengJiaoJanuaryNum + 1;
                }else if (monthValue == 2) {
                    chengJiaoFebruaryNum = chengJiaoFebruaryNum + 1;
                }else if (monthValue == 3) {
                    chengJiaoMarchNum = chengJiaoMarchNum + 1;
                }else if (monthValue == 4) {
                    chengJiaoSprilNum = chengJiaoSprilNum + 1;
                }else if (monthValue == 5) {
                    chengJiaoMayNum = chengJiaoMayNum + 1;
                }else if (monthValue == 6) {
                    chengJiaoJuneNum = chengJiaoJuneNum + 1;
                }else if (monthValue == 7) {
                    chengJiaoJulyNum = chengJiaoJulyNum + 1;
                }else if (monthValue == 8) {
                    chengJiaoAugustNum = chengJiaoAugustNum + 1;
                }else if (monthValue == 9) {
                    chengJiaoSeptemberNum = chengJiaoSeptemberNum + 1;
                }else if (monthValue == 10){
                    chengJiaoOctoberNum = chengJiaoOctoberNum + 1;
                }else if (monthValue == 11){
                    chengJiaoNovemberNum = chengJiaoNovemberNum + 1;
                }else if (monthValue == 12){
                    chengJiaoDecemberNum = chengJiaoDecemberNum + 1;
                }
            }
            if("回族镇".equals(illegalBuildPO.getTownStreet())){
                if("扩建".equals(illegalBuildPO.getIllegalBuildType())){
                    huiZuExtensionNum = huiZuExtensionNum + 1;
                }else if("加建".equals(illegalBuildPO.getIllegalBuildType())){
                    huiZuAddConNum = huiZuAddConNum + 1;
                }else if("超占".equals(illegalBuildPO.getIllegalBuildType())){
                    huiZuSuperNum = huiZuSuperNum + 1;
                }
                Month month = illegalBuildPO.getSupervisionDate().getMonth();
                int monthValue = month.getValue();
                if(monthValue == 1){
                    huiZuJanuaryNum = huiZuJanuaryNum + 1;
                }else if (monthValue == 2) {
                    huiZuFebruaryNum = huiZuFebruaryNum + 1;
                }else if (monthValue == 3) {
                    huiZuMarchNum = huiZuMarchNum + 1;
                }else if (monthValue == 4) {
                    huiZuSprilNum = huiZuSprilNum + 1;
                }else if (monthValue == 5) {
                    huiZuMayNum = huiZuMayNum + 1;
                }else if (monthValue == 6) {
                    huiZuJuneNum = huiZuJuneNum + 1;
                }else if (monthValue == 7) {
                    huiZuJulyNum = huiZuJulyNum + 1;
                }else if (monthValue == 8) {
                    huiZuAugustNum = huiZuAugustNum + 1;
                }else if (monthValue == 9) {
                    huiZuSeptemberNum = huiZuSeptemberNum + 1;
                }else if (monthValue == 10){
                    huiZuOctoberNum = huiZuOctoberNum + 1;
                }else if (monthValue == 11){
                    huiZuNovemberNum = huiZuNovemberNum + 1;
                }else if (monthValue == 12){
                    huiZuDecemberNum = huiZuDecemberNum + 1;
                }
            }
        }
        //内层map统计每个地区三类数量
        illegalBuildYongNingMap.put("扩建", (double)yongNingExtensionNum);
        illegalBuildYongNingMap.put("加建", (double)yongNingAddConNum);
        illegalBuildYongNingMap.put("超占", (double)yongNingSuperNum);

        illegalBuildChengJiaoMap.put("扩建", (double)chengJiaoExtensionNum);
        illegalBuildChengJiaoMap.put("加建", (double)chengJiaoAddConNum);
        illegalBuildChengJiaoMap.put("超占", (double)chengJiaoSuperNum);

        illegalBuildHuiZuMap.put("扩建", (double)huiZuExtensionNum);
        illegalBuildHuiZuMap.put("加建", (double)huiZuAddConNum);
        illegalBuildHuiZuMap.put("超占", (double)huiZuSuperNum);
        //外层map合并三个地区map
        illegalBuildDistributionMap.put("永宁街道", illegalBuildYongNingMap);
        illegalBuildDistributionMap.put("城郊乡", illegalBuildChengJiaoMap);
        illegalBuildDistributionMap.put("回族镇", illegalBuildHuiZuMap);

        illegalBuildDistributionMap.put("永宁街道", illegalBuildYongNingMap);
        illegalBuildDistributionMap.put("城郊乡", illegalBuildChengJiaoMap);
        illegalBuildDistributionMap.put("回族镇", illegalBuildHuiZuMap);

        //-----------------------------------------------------------
        //1-12月数量
        illegalBuildYongNingMonthMap.put("一月", (double)yongNingJanuaryNum);
        illegalBuildYongNingMonthMap.put("二月", (double)yongNingFebruaryNum);
        illegalBuildYongNingMonthMap.put("三月", (double)yongNingMarchNum);
        illegalBuildYongNingMonthMap.put("四月", (double)yongNingSprilNum);
        illegalBuildYongNingMonthMap.put("五月", (double)yongNingMayNum);
        illegalBuildYongNingMonthMap.put("六月", (double)yongNingJuneNum);
        illegalBuildYongNingMonthMap.put("七月", (double)yongNingJulyNum);
        illegalBuildYongNingMonthMap.put("八月", (double)yongNingAugustNum);
        illegalBuildYongNingMonthMap.put("九月", (double)yongNingSeptemberNum);
        illegalBuildYongNingMonthMap.put("十月", (double)yongNingOctoberNum);
        illegalBuildYongNingMonthMap.put("十一月", (double)yongNingNovemberNum);
        illegalBuildYongNingMonthMap.put("十二月", (double)yongNingDecemberNum);

        illegalBuildChengJiaoMonthMap.put("一月", (double)chengJiaoJanuaryNum);
        illegalBuildChengJiaoMonthMap.put("二月", (double)chengJiaoFebruaryNum);
        illegalBuildChengJiaoMonthMap.put("三月", (double)chengJiaoMarchNum);
        illegalBuildChengJiaoMonthMap.put("四月", (double)chengJiaoSprilNum);
        illegalBuildChengJiaoMonthMap.put("五月", (double)chengJiaoMayNum);
        illegalBuildChengJiaoMonthMap.put("六月", (double)chengJiaoJuneNum);
        illegalBuildChengJiaoMonthMap.put("七月", (double)chengJiaoJulyNum);
        illegalBuildChengJiaoMonthMap.put("八月", (double)chengJiaoAugustNum);
        illegalBuildChengJiaoMonthMap.put("九月", (double)chengJiaoSeptemberNum);
        illegalBuildChengJiaoMonthMap.put("十月", (double)chengJiaoOctoberNum);
        illegalBuildChengJiaoMonthMap.put("十一月", (double)chengJiaoNovemberNum);
        illegalBuildChengJiaoMonthMap.put("十二月", (double)chengJiaoDecemberNum);

        illegalBuildHuiZuMonthMap.put("一月", (double)huiZuJanuaryNum);
        illegalBuildHuiZuMonthMap.put("二月", (double)huiZuFebruaryNum);
        illegalBuildHuiZuMonthMap.put("三月", (double)huiZuMarchNum);
        illegalBuildHuiZuMonthMap.put("四月", (double)huiZuSprilNum);
        illegalBuildHuiZuMonthMap.put("五月", (double)huiZuMayNum);
        illegalBuildHuiZuMonthMap.put("六月", (double)huiZuJuneNum);
        illegalBuildHuiZuMonthMap.put("七月", (double)huiZuJulyNum);
        illegalBuildHuiZuMonthMap.put("八月", (double)huiZuAugustNum);
        illegalBuildHuiZuMonthMap.put("九月", (double)huiZuSeptemberNum);
        illegalBuildHuiZuMonthMap.put("十月", (double)huiZuOctoberNum);
        illegalBuildHuiZuMonthMap.put("十一月", (double)huiZuNovemberNum);
        illegalBuildHuiZuMonthMap.put("十二月", (double)huiZuDecemberNum);

        illegalBuildAreaMap.put("永宁街道", illegalBuildYongNingMonthMap);
        illegalBuildAreaMap.put("城郊乡", illegalBuildChengJiaoMonthMap);
        illegalBuildAreaMap.put("回族镇", illegalBuildHuiZuMonthMap);

        //-----------------------------------------------------------
        illegalBuildStatusMap.put("未开始", beforeNum);
        illegalBuildStatusMap.put("进行中", midNum);
        illegalBuildStatusMap.put("已完成", afterNum);

        finishedRateNum = (double )finishedSumNum/taskSumNum;
        IllegalBuildstatisticsVO illegalBuildstatisticsVO = new IllegalBuildstatisticsVO();
        illegalBuildstatisticsVO.setTaskSum((double )taskSumNum);
        illegalBuildstatisticsVO.setFinishedToday((double )finishedTodayNum);
        illegalBuildstatisticsVO.setFinishedSum((double )finishedSumNum);
        illegalBuildstatisticsVO.setFinishedRate(finishedRateNum);
        illegalBuildstatisticsVO.setIllegalBuildDistributionMap(illegalBuildDistributionMap);
        illegalBuildstatisticsVO.setIllegalBuildAreaMap(illegalBuildAreaMap);
        illegalBuildstatisticsVO.setIllegalBuildStatusMap(illegalBuildStatusMap);
        return illegalBuildstatisticsVO;
    }

    @Override
    public ApiResp addOrUpdateIllegalBuild(IllegalBuildDTO illegalBuildDTO) {
        ApiResp apiResp = new ApiResp();
        IllegalBuildPO illegalBuildPO = null;
        //图片列表转换为图片在服务器的路径
        if(!StringUtils.isEmpty(illegalBuildDTO.getBeforeImages())){
            List<MultipartFile> beforeImages = illegalBuildDTO.getBeforeImages();
            String beforeImagesPath = FileTransUtils.uploadFiles(beforeImages, fileUpload);
            illegalBuildDTO.setBeforeImagesPath(beforeImagesPath);
        }
        if(!StringUtils.isEmpty(illegalBuildDTO.getMidImages())){
            List<MultipartFile> midImages = illegalBuildDTO.getMidImages();
            String midImagesPath = FileTransUtils.uploadFiles(midImages, fileUpload);
            illegalBuildDTO.setMidImagesPath(midImagesPath);
        }
        if(!StringUtils.isEmpty(illegalBuildDTO.getAfterImages())){
            List<MultipartFile> afterImages = illegalBuildDTO.getAfterImages();
            String afterImagesPath = FileTransUtils.uploadFiles(afterImages, fileUpload);
            illegalBuildDTO.setAfterImagesPath(afterImagesPath);
        }
        //判断新增或者更新
        QueryWrapper<IllegalBuildPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("illegal_build_code", illegalBuildDTO.getIllegalBuildCode());
        // 查询记录数量
        Integer count = illegalBuildMapper.selectCount(queryWrapper);
        illegalBuildPO = illegalBuildDTO2PO(illegalBuildDTO);
        if(count <= 0){
            UUID uuid = UUID.randomUUID();
            illegalBuildPO.setIllegalBuildCode(String.valueOf(uuid));
            int addResult = illegalBuildMapper.insert(illegalBuildPO);
            if(addResult > 0){
                apiResp.setStatus(200);
                apiResp.setMessage("新增成功");
            }else {
                apiResp.setStatus(201);
                apiResp.setMessage("新增失败");
            }
        }else{
            UpdateWrapper<IllegalBuildPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("illegal_build_code", illegalBuildDTO.getIllegalBuildCode());
            int updateResult = illegalBuildMapper.update(illegalBuildPO, updateWrapper);
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
    public ApiResp daleteIllegalBuild(String illegalBuildCode) {
        ApiResp apiResp = new ApiResp();
        QueryWrapper<IllegalBuildPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("illegal_build_code", illegalBuildCode);
        // 查询记录数量
        Integer count = illegalBuildMapper.selectCount(queryWrapper);
        if(count <= 0){
            apiResp.setStatus(203);
            apiResp.setMessage("数据不存在");
        }else{
            UpdateWrapper<IllegalBuildPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("illegal_build_code", illegalBuildCode);
            int deleteResult = illegalBuildMapper.delete(updateWrapper);
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

    /**
    * 分页查询
    *
    * @param current 当前页码
    * @param size  每页大小
    * @return
    */
    @Override
    public Page<IllegalBuildBO> paginQuery(String illegalBuildType, String illegalBuildStatus, long current, long size){
        //执行分页查询
        Page<IllegalBuildPO> pagin = new Page<>(current , size , true);
        IPage<IllegalBuildPO> selectResult = illegalBuildMapper.selectByPage(pagin, illegalBuildType, illegalBuildStatus);
        pagin.setPages(selectResult.getPages());
        pagin.setTotal(selectResult.getTotal());
        pagin.setRecords(selectResult.getRecords());
        //返回结果
        return IllegalBuildConverter.INSTANCE.toIllegalBuildBOPageByPO(pagin);
    }

    private IllegalBuildPO illegalBuildDTO2PO(IllegalBuildDTO dto){
        IllegalBuildPO po = new IllegalBuildPO();
        po.setIllegalBuildCode(dto.getIllegalBuildCode());
        po.setIllegalBuildType(dto.getIllegalBuildType());
        po.setIllegalBuildStatus(dto.getIllegalBuildStatus());
        po.setBuildStaff(dto.getBuildStaff());
        po.setAddress(dto.getAddress());
        po.setBuildArea(dto.getBuildArea());
        po.setDemolishedArea(dto.getDemolishedArea());
        po.setSupervisionDate(dto.getSupervisionDate());
        po.setResponsiblePerson(dto.getResponsiblePerson());
        po.setCurrentDesc(dto.getCurrentDesc());
        po.setRecommendedMeasure(dto.getRecommendedMeasure());
        po.setEnforcementStartDate(dto.getEnforcementStartDate());
        po.setEnforcementEndDate(dto.getEnforcementEndDate());
        po.setProgress(dto.getProgress());
        po.setPhotoPeriod(dto.getPhotoPeriod());
        po.setBeforeImagesPath(dto.getBeforeImagesPath());
        po.setMidImagesPath(dto.getMidImagesPath());
        po.setAfterImagesPath(dto.getAfterImagesPath());
        po.setLongitude(dto.getLongitude());
        po.setLatitude(dto.getLatitude());
        po.setHeight(dto.getHeight());
        po.setTownStreet(dto.getTownStreet());
        return po;
    }
}