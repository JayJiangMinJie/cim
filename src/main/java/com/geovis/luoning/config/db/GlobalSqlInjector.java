package com.geovis.luoning.config.db;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * sql 注入器 --实现自定义sql查询方法
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/9 9:35
 */
@Component
public class GlobalSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留MP自带方法 可以super.getMethodList() 再add
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return super.getMethodList(mapperClass);
    }
}
