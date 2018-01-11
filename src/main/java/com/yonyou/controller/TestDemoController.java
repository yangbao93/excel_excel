package com.yonyou.controller;

import com.yonyou.entity.Demo;
import com.yonyou.service.DemoService;
import com.yonyou.service.component.ParseExecelService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * demo
 * Created by yangbao on 2017/11/28.
 */
@RestController
@Slf4j
public class TestDemoController {

  private static Logger logger = LoggerFactory.getLogger(TestDemoController.class);

  @Autowired
  private DemoService service;

  @Autowired
  private ParseExecelService parseExecelService;

  @RequestMapping(value = "demo", method = RequestMethod.POST)
  public void demo() {
    log.debug("测试lombok的功能");
    logger.debug("logger 的样式");
    System.out.println("this is controller!");
    service.say();
  }

  @RequestMapping(value = "readExcel",method = RequestMethod.POST)
  public void readExcel(){
    try {
      InputStream is = new FileInputStream(new File("C:\\Users\\Administrator\\Downloads\\电子发票导入模板.xls"));
      parseExecelService.parseExcel(null,null);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "getInfo",method = RequestMethod.POST)
  public void getInfo() {
    service.getInfo();
  }

}
