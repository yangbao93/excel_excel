package com.yonyou.controller;

import java.io.*;
import java.util.List;

import com.yonyou.entity.ConfigEntity;
import com.yonyou.service.ConfigurationService;
import com.yonyou.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.service.component.ReadExcelValueService;

/**
 * 读取excel中的内容 Created by yangbao on 2017/12/21.
 */
@RestController
@RequestMapping("/readexcel")
public class TestReadExcelController {

  @Autowired
  private ReadExcelValueService readExcelService;

  @Autowired
  private ExcelService service;

  @Autowired
  private ConfigurationService configurationService;

  @RequestMapping(value = "/read", method = RequestMethod.POST)
  public void readExcel() {
    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("C:\\Users\\杨堡\\Desktop\\parse.xlsx")))) {
      List<ConfigEntity> entityList = configurationService.selectConfigEntityList("1", 1);
      List<Object> objects = service.readExcelValue(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
