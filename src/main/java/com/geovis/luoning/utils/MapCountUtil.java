package com.geovis.luoning.utils;

import java.math.BigDecimal;
 
/**
 * 地图计算工具类
 */
public class MapCountUtil {
 
 
    /**
     *
     * @Description   计算给定经纬度附近相应公里数的经纬度范围
     * @param         longitude 经度
     * @param         latitude 纬度
     * @param         distance 距离（千米）
     * @return        String 格式：经度最小值-经度最大值-纬度最小值-纬度最大值
     * @Data          2020.06.19
     **/
    public static String getNearbyByLongitudeAndLatitudeAndDistance(BigDecimal longitude, BigDecimal latitude, Integer distance) {
        // 地球半径千米
        double r = 6371.393;
        double lng = longitude.doubleValue();
        double lat = latitude.doubleValue();
        double dlng = 2 * Math.asin(Math.sin(distance / (2 * r)) / Math.cos(lat * Math.PI / 180));
        // 角度转为弧度
        dlng = dlng * 180 / Math.PI;
        double dlat = distance / r;
        dlat = dlat * 180 / Math.PI;
        double minlat = lat - dlat;
        double maxlat = lat + dlat;
        double minlng = lng - dlng;
        double maxlng = lng + dlng;
 
        return minlng + "-" + maxlng + "-" + minlat + "-" + maxlat;
    }
 
    /**
     * @Description     根据经纬度获取两点之间的距离
     * @param           longitude1   地点1经度
     * @param           latitude1    地点1纬度
     * @param           longitude2   地点2经度
     * @param           latitude2    地点2纬度
     * @return          距离：单位 米
     */
    public static Double getDistance(BigDecimal longitude1, BigDecimal latitude1, BigDecimal longitude2, BigDecimal latitude2) {
        // 地球半径千米
        double r = 6371.393;
        double lat1 = latitude1.doubleValue();
        double lng1 = longitude1.doubleValue();
        double lat2 = latitude2.doubleValue();
        double lng2 = longitude2.doubleValue();
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * r;
        s = Math.round(s * 1000);
        return s;
    }
 
    private static Double rad(double d) {
        return d * Math.PI / 180.0;
    }
}