package com.geovis.luoning.utils;

import java.io.File;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.w3c.dom.Attr;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.DocumentBuilder;

public class XmlToExcelConverter {

   private static final String xmlFile = "D:\\项目数据汇总\\临沂项目\\filepath\\reportXml\\HTMLtoXML.xml";

   private static final String excelFile = "D:\\项目数据汇总\\临沂项目\\filepath\\reportExcel\\HTMLtoexcel.xlsx";

   

   public static void main(String[] args) throws Exception {

   

      Workbook workbook = new XSSFWorkbook(); // 创建一个新的Excel工作簿

      Sheet sheet = workbook.createSheet("Sheet1"); // 创建一个新的工作表

      

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      Document doc = dBuilder.parse(new File(xmlFile));

      

      NodeList nodeList = doc.getElementsByTagName("*"); // 获取XML文件中的所有元素

      int rowIndex = 0;

      for (int i = 0; i < nodeList.getLength(); i++) {

         Node node = nodeList.item(i);

         if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element)node;

            Row row = sheet.createRow(rowIndex++); // 创建一个新行

            int cellIndex = 0;

            for (int j = 0; j < element.getAttributes().getLength(); j++) {

               Attr attr = (Attr)element.getAttributes().item(j);

               Cell cell = row.createCell(cellIndex++); // 创建一个新单元格

               cell.setCellValue(attr.getValue()); // 设置单元格的值

            }

         }

      }

      

      FileOutputStream fileOut = new FileOutputStream(excelFile);

      workbook.write(fileOut);

      fileOut.close();

      workbook.close();

   }

}