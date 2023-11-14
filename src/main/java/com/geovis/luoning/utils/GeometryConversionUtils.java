package com.geovis.luoning.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgis.Geometry;
import org.postgis.PGgeometry;

public class GeometryConversionUtils {

    public static String getWkt(String geoJsonString) {
        String wkt = "";
        // 使用Jackson库解析GeoJSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(geoJsonString);
            JsonNode coordinatesNode = jsonNode.get("coordinates");

            // 提取坐标数据并构建WKT字符串
            StringBuilder wktBuilder = new StringBuilder("POLYGON((");
            for (JsonNode pointNode : coordinatesNode.get(0)) {
                double lon = pointNode.get(0).asDouble();
                double lat = pointNode.get(1).asDouble();
                wktBuilder.append(lon).append(" ").append(lat).append(",");
            }
            // 添加第一个坐标以闭合多边形
            wktBuilder.append(coordinatesNode.get(0).get(0).get(0).asDouble());
            wktBuilder.append(" ");
            wktBuilder.append(coordinatesNode.get(0).get(0).get(1).asDouble());
            wktBuilder.append("))");

            // 创建Geometry对象
            PGgeometry pgGeometry = new PGgeometry(wktBuilder.toString());
            Geometry geometry = pgGeometry.getGeometry();
            wkt = geometry.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 打印Geometry对象
        return wkt;
    }
}
