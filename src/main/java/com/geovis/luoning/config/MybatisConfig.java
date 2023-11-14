package com.geovis.luoning.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Mapper Config自定义配置
 *
 * @author jay
 * @version V1.0
 * @date 2022/8/30 15:33
 */
@Configuration
@MapperScan(basePackages = {"com.geovis.luoning.mapper"})
public class MybatisConfig {
    /**
     /**
     * MyBatisPlus拦截器（用于分页）
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //添加pgsql的分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

//    // 多数据源配置
//    @Bean(name = "dataSource1")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "dataSource2")
//    @ConfigurationProperties(prefix = "spring.datasource.db2")
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }
//
//    // Mybatis-Plus多数据源配置
//    @Bean
//    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("mysql1") DataSource dataSource1,
//                                                          @Qualifier("mysql2") DataSource dataSource2) throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(this.dynamicDataSource(dataSource1, dataSource2));
//
//        // Mybatis-Plus全局配置
//        GlobalConfig globalConfig = new GlobalConfig();
//        globalConfig.setSqlInjector(new LogicSqlInjector());
//        sqlSessionFactory.setGlobalConfig(globalConfig);
//
//        return sqlSessionFactory;
//    }
//
//    // 动态数据源
//    @Bean
//    public DynamicDataSource dynamicDataSource(@Qualifier("mysql1") DataSource dataSource1,
//                                               @Qualifier("mysql2") DataSource dataSource2) {
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put(DataSourceEnum.DATA_SOURCE1, dataSource1);
//        targetDataSources.put(DataSourceEnum.DATA_SOURCE2, dataSource2);
//
//        DynamicDataSource dynamicDataSource = new DynamicDataSource();
//        dynamicDataSource.setTargetDataSources(targetDataSources);
//        dynamicDataSource.setDefaultTargetDataSource(dataSource1);
//
//        return dynamicDataSource;
//    }

}
