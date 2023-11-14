package com.geovis.luoning;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

/**
 * @author jay
 * @version 1.0.0
 * @date 2022-08-23
 */
@SpringBootApplication
@MapperScan("com.geovis.luoning.mapper")
public class StartApplication {

    private static Logger logger = LoggerFactory.getLogger(StartApplication.class);

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch("Spring Boot Application");
        stopWatch.start("application");
        SpringApplication.run(StartApplication.class, args);
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
    }

}