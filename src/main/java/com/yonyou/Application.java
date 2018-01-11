package com.yonyou;

import ch.qos.logback.core.db.DataSourceConnectionSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 主方法
 * Created by yangbao on 2017/11/28.
 */
@SpringBootApplication
@ComponentScan(value = "com.yonyou.*")
@MapperScan(value = "com.yonyou.dao")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
