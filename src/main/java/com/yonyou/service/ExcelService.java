package com.yonyou.service;

import com.yonyou.entity.ExcelExportEntity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public interface ExcelService {


  /**
   * excel转换为java对象
   * 
   * @param inputStream 输入流
   * @return 对象的list
   */
  List<Object> readExcelValue(InputStream inputStream);

  /**
   * 导出excel模板
   * 
   * @param corpid 公司id
   * @param type 模板类型
   * @return 返回工作簿
   */
  HSSFWorkbook exportExcelTemplate(String corpid, int type);

  /**
   * 导出数据
   * 
   * @param exportEntity 导出实体
   * @return 返回工作簿
   */
  HSSFWorkbook exportExcelData(ExcelExportEntity exportEntity);
}
