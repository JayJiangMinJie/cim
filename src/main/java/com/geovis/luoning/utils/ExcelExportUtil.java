package com.geovis.luoning.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geovis.luoning.mapper.ConstructionPlanMapper;
import com.geovis.luoning.mapper.IllegalBuildMapper;
import com.geovis.luoning.po.ConstructionPlanPO;
import com.geovis.luoning.po.IllegalBuildPO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class ExcelExportUtil {

    @Autowired
    private IllegalBuildMapper illegalBuildMapper;

    @Autowired
    private ConstructionPlanMapper constructionPlanMapper;

    @Value("${file.excelIll}")
    private String excelExportPathIll;

    @Value("${file.excelCon}")
    private String excelExportPathCon;

    public String exportConstructionPlanDataToExcel(String searchContent) throws IOException {

        // 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // 设置表头
        String[] headers = {"id", "plan_code", "land_use", "land_use_code", "address", "land_area", "images_path", "plan_geo"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        // 从数据库中查询数据
        List<ConstructionPlanPO> dataList = constructionPlanMapper.selectAllConstructionPlan(searchContent);

        // 将数据写入Excel表格
        int rowNum = 1;
        for (ConstructionPlanPO data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId()); // 根据你的实体类字段名称设置
            row.createCell(1).setCellValue(data.getPlanCode());
            row.createCell(2).setCellValue(data.getLandUse());
            row.createCell(3).setCellValue(data.getLandUseCode());
            row.createCell(4).setCellValue(data.getAddress());
            row.createCell(5).setCellValue(data.getLandArea());
            row.createCell(6).setCellValue(data.getImagesPath());
            row.createCell(7).setCellValue(data.getPlanGeo().toString());
        }

        // 生成导出文件的文件名（可以根据需要自定义）
        String fileExtension = ".xlsx"; // 文件扩展名（可以根据需要修改）
        String randomFileName = generateRandomFileName(fileExtension);

        // 定义导出文件的完整路径
        String exportFilePath = excelExportPathCon + "/" + randomFileName;

        // 将工作簿写入文件
        try (FileOutputStream fileOutputStream = new FileOutputStream(exportFilePath)) {
            workbook.write(fileOutputStream);
        }

        // 关闭工作簿
        workbook.close();

        // 返回导出文件的路径
        return exportFilePath;
    }

    public String exportIllegalBuildDataToExcel(String illegalBuildType, String illegalBuildStatus) throws IOException {

        // 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // 设置表头
        String[] headers = {"id", "illegal_build_type", "illegal_build_statu", "constructor", "address", "build_area", "demolished_area", "supervision_date",
                "responsible_person", "current_desc", "recommended_measure", "enforcement_start_date", "enforcement_end_date", "progress", "photo_period", "before_images_path",
                "mid_images_path", "after_images_path", "longitude", "latitude", "height", "illegal_build_code"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(illegalBuildType!= null, "illegal_build_type", illegalBuildType);
        queryWrapper.eq(illegalBuildStatus!= null, "illegal_build_status", illegalBuildStatus);
        // 从数据库中查询数据
        List<IllegalBuildPO> dataList = illegalBuildMapper.selectList(queryWrapper);

        // 将数据写入Excel表格
        int rowNum = 1;
        for (IllegalBuildPO data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId()); // 根据你的实体类字段名称设置
            row.createCell(1).setCellValue(data.getIllegalBuildType());
            row.createCell(2).setCellValue(data.getIllegalBuildStatus());
            row.createCell(3).setCellValue(data.getBuildStaff());
            row.createCell(4).setCellValue(data.getAddress());
            row.createCell(5).setCellValue(data.getBuildArea());
            row.createCell(6).setCellValue(data.getDemolishedArea());
            row.createCell(7).setCellValue(data.getSupervisionDate());
            row.createCell(8).setCellValue(data.getResponsiblePerson());
            row.createCell(9).setCellValue(data.getCurrentDesc());
            row.createCell(10).setCellValue(data.getRecommendedMeasure());
            row.createCell(11).setCellValue(data.getEnforcementStartDate());
            row.createCell(12).setCellValue(data.getEnforcementEndDate());
            row.createCell(13).setCellValue(data.getProgress());
            row.createCell(14).setCellValue(data.getPhotoPeriod());
            row.createCell(15).setCellValue(data.getBeforeImagesPath());
            row.createCell(16).setCellValue(data.getMidImagesPath());
            row.createCell(17).setCellValue(data.getAfterImagesPath());
            row.createCell(18).setCellValue(data.getLongitude());
            row.createCell(19).setCellValue(data.getLatitude());
            row.createCell(20).setCellValue(data.getHeight());
            row.createCell(21).setCellValue(data.getIllegalBuildCode());
        }

        // 生成导出文件的文件名（可以根据需要自定义）
        String fileExtension = ".xlsx"; // 文件扩展名（可以根据需要修改）
        String randomFileName = generateRandomFileName(fileExtension);

        // 定义导出文件的完整路径
        String exportFilePath = excelExportPathIll + "/" + randomFileName;

        // 将工作簿写入文件
        try (FileOutputStream fileOutputStream = new FileOutputStream(exportFilePath)) {
            workbook.write(fileOutputStream);
        }

        // 关闭工作簿
        workbook.close();

        // 返回导出文件的路径
        return exportFilePath;
    }

    public static String generateRandomFileName(String fileExtension) {
        // 生成一个随机的UUID作为文件名的一部分
        String randomUUID = UUID.randomUUID().toString();

        // 将文件扩展名添加到文件名中（例如，.xlsx）
        String fileName = randomUUID + fileExtension;

        return fileName;
    }
}
