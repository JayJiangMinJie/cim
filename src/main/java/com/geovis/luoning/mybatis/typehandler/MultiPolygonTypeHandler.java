package com.geovis.luoning.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgis.MultiPolygon;

@MappedTypes({MultiPolygon.class})
@MappedJdbcTypes(JdbcType.OTHER)
public class MultiPolygonTypeHandler extends AbstractGeometryTypeHandler<MultiPolygon> {


}
