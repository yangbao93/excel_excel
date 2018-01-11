package com.yonyou.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.entity.ExcelExportEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yonyou.entity.ConfigEntity;
import com.yonyou.service.ConfigurationService;
import com.yonyou.service.ExcelService;
import com.yonyou.util.CheckFireFoxUtil;

/**
 * 用于外部调用的excel服务 Created by yangbao on 2017/12/22.
 * @author yangbao
 */
@RestController
@RequestMapping("excel")
@Slf4j
public class ExcelController {

  @Autowired
  private ExcelService excelService;

  @Autowired
  private ConfigurationService configurationService;

  @RequestMapping(value = "exporttemplate", method = RequestMethod.GET)
  public void exportExcelTemplate(@RequestParam String corpid, @RequestParam int type,
      HttpServletRequest request, HttpServletResponse response) {
    log.debug("excel模板导出功能");
    // 查询出导出模板的名称
    List<ConfigEntity> configEntities = configurationService.selectConfigEntityList(corpid, type);
    // 查询模板并写入excel的workbook
    log.debug("获取excel模板");
    HSSFWorkbook workbook = excelService.exportExcelTemplate(corpid, type);
    // 将workbook的内容写入response中,返回
    try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
      String fileName = configEntities.get(0).getEntity_name() + ".xlsx";
      fileName = URLEncoder.encode(fileName, "utf-8");
      // 检查是否是火狐浏览器
      log.debug("检查是否火狐浏览器");
      if (CheckFireFoxUtil.isFireFox(request)) {
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
      } else {
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
      }
      response.setContentType("application/vnd.ms-excel");
      workbook.write(out);
      out.flush();
      log.debug(String.format("%s的excel模板导出完成", URLDecoder.decode(fileName,"utf-8")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/export-data", method = RequestMethod.POST)
  public void exportDataWithMap(@RequestBody ExcelExportEntity exportEntity) {

      log.debug("导出数据");
      HSSFWorkbook workbook = excelService.exportExcelData(exportEntity);

  }
}
