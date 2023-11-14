package com.geovis.luoning.utils;

import java.awt.geom.Point2D;

public class ScopeUtils {

//    /**
//     * 判断点是否在多边形内，如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
//     * @return      点在多边形内返回true,否则返回false
//     */
//    public static boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> pts){
//
//        int N = pts.size();
//        //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
//        boolean boundOrVertex = true;
//        //cross points count of x
//        int intersectCount = 0;
//        //浮点类型计算时候与0比较时候的容差
//        double precision = 2e-10;
//        //neighbour bound vertices
//        Point2D.Double p1, p2;
//        Point2D.Double p = point;
//
//        //当前点
//        p1 = pts.get(0);
//        //left vertex
//        for(int i = 1; i <= N; ++i){
//            //check all rays
//            if(p.equals(p1)){
//                //p is an vertex
//                return boundOrVertex;
//
//            }
//
//            p2 = pts.get(i % N);
//            //right vertex
//            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){
//                //ray is outside of our interests
//                p1 = p2;
//                continue;//next ray left point
//            }
//
//            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){
//                //ray is crossing over by the algorithm (common part of)
//                if(p.y <= Math.max(p1.y, p2.y)){
//                    //x is before of ray
//                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){
//                        //overlies on a horizontal ray
//                        return boundOrVertex;
//                    }
//
//                    if(p1.y == p2.y){
//                        //ray is vertical
//                        if(p1.y == p.y){
//                            //overlies on a vertical ray
//                            return boundOrVertex;
//                        }else{
//                            //before ray
//                            ++intersectCount;
//                        }
//                    }else{//cross point on the left side
//                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
//                        //cross point of y
//                        if(Math.abs(p.y - xinters) < precision){
//                            //overlies on a ray
//                            return boundOrVertex;
//                        }
//
//                        if(p.y < xinters){
//                            //before ray
//                            ++intersectCount;
//                        }
//                    }
//                }
//            }else{
//                //special case when ray is crossing through the vertex
//                if(p.x == p2.x && p.y <= p2.y){
//                    //p crossing over p2
//                    Point2D.Double p3 = pts.get((i+1) % N);
//                    //next vertex
//                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){
//                        //p.x lies between p1.x & p3.x
//                        ++intersectCount;
//                    }else{
//                        intersectCount += 2;
//                    }
//                }
//            }
//            p1 = p2;
//            //next ray left point
//        }
//
//        if(intersectCount % 2 == 0){
//            //偶数在多边形外
//            return false;
//        } else {
//            //奇数在多边形内
//            return true;
//        }
//    }
//
//    /**
//     *
//     * @param x 点位 x  例：50
//     * @param y 点位 y  例：50
//     * @param areaCoordinates 多边形坐标  例：“0,0,100,0,100,100,0,100”
//     * @return
//     */
//    public static boolean IsPtInPoly(java.lang.Double x, java.lang.Double y,String areaCoordinates){
//        Point2D.Double point = new Point2D.Double(x,y);//测试点位坐标
//        String areaCoordinateslist[] = areaCoordinates.split(",");
//        List<Point2D.Double> pts = new ArrayList<>();
//
//
//        if(areaCoordinateslist.length%2==0){
//            for (int i = 0;i<areaCoordinateslist.length/2;i++){
//                int min  = i*2;
//                int max  =i*2+1;
//                pts.add(new Point2D.Double(java.lang.Double.parseDouble(areaCoordinateslist[min]), java.lang.Double.parseDouble(areaCoordinateslist[max])));
//            }
//        }
//
//        int N = pts.size();
//        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
//        int intersectCount = 0;//cross points count of x
//        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
//        Point2D.Double p1, p2;//neighbour bound vertices
//        Point2D.Double p = point; //当前点
//
//        p1 = pts.get(0);//left vertex
//        for(int i = 1; i <= N; ++i){//check all rays
//            if(p.equals(p1)){
//                return boundOrVertex;//p is an vertex
//            }
//
//            p2 = pts.get(i % N);//right vertex
//            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests
//                p1 = p2;
//                continue;//next ray left point
//            }
//
//            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
//                if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray
//                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
//                        return boundOrVertex;
//                    }
//
//                    if(p1.y == p2.y){//ray is vertical
//                        if(p1.y == p.y){//overlies on a vertical ray
//                            return boundOrVertex;
//                        }else{//before ray
//                            ++intersectCount;
//                        }
//                    }else{//cross point on the left side
//                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y
//                        if(Math.abs(p.y - xinters) < precision){//overlies on a ray
//                            return boundOrVertex;
//                        }
//                        if(p.y < xinters){//before ray
//                            ++intersectCount;
//                        }
//                    }
//                }
//            }else{//special case when ray is crossing through the vertex
//                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2
//                    Point2D.Double p3 = pts.get((i+1) % N); //next vertex
//                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
//                        ++intersectCount;
//                    }else{
//                        intersectCount += 2;
//                    }
//                }
//            }
//            p1 = p2;//next ray left point
//        }
//
//        if(intersectCount % 2 == 0){//偶数在多边形外
//            return false;
//        } else { //奇数在多边形内
//            return true;
//        }
//
//    }

    public static boolean isPtInPoly (double ALon , double ALat , Point2D.Double[] ps) {
        int iSum, iCount, iIndex;
        double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;
        if (ps.length < 3) {
            return false;
        }
        iSum = 0;
        iCount = ps.length;
        for (iIndex = 0; iIndex<iCount;iIndex++) {
            if (iIndex == iCount - 1) {
                dLon1 = ps[iIndex].getX();
                dLat1 = ps[iIndex].getY();
                dLon2 = ps[0].getX();
                dLat2 = ps[0].getY();
            } else {
                dLon1 = ps[iIndex].getX();
                dLat1 = ps[iIndex].getY();
                dLon2 = ps[iIndex + 1].getX();
                dLat2 = ps[iIndex + 1].getY();
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    //得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat) ) / (dLat1 - dLat2);
                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                    if (dLon < ALon) {
                        iSum++;
                    }
                }
            }
        }
        if ((iSum % 2) != 0) {
            return true;
        }
        return false;
    }

}
