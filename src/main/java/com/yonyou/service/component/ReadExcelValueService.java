package com.yonyou.service.component;

import com.yonyou.entity.ConfigEntity;
import com.yonyou.util.BusinessRuntimeException;
import groovy.util.logging.Log4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读取excel中所有的数据项
 * (从第一行开始读取一直读取到最后一行(最后一行的判断为全部数据为空))
 * Created by yangbao on 2017/12/15.
 */
@Component
public class ReadExcelValueService {

  private static Logger logger = LoggerFactory.getLogger(ReadExcelValueService.class);

  public List<List<String>> init(InputStream fis) {
    logger.debug("初始化excel文件，获取workbook");
    if (fis == null){
      return null;
    }
    Workbook workbook = null;
    try {
      if (POIFSFileSystem.hasPOIFSHeader(fis)) {
        workbook = new HSSFWorkbook(fis);
      } else if (POIXMLDocument.hasOOXMLHeader(fis)) {
        workbook = new XSSFWorkbook(fis);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 获取sheet的个数
    int numberOfSheets = workbook.getNumberOfSheets();
    List<ConfigEntity> configList = new ArrayList<ConfigEntity>();
    // 存放整个excel的值
    List<List<String>> allExcelRowValue = new ArrayList<List<String>>();
    logger.debug("循环遍历所有的sheet");
    for (int i = 0; i < numberOfSheets; i++) {
      Sheet sheet = workbook.getSheetAt(i);
      if (sheet.getFirstRowNum() == sheet.getLastRowNum()){
        logger.debug("第一行和最后一行相等,非法的格式");
        return allExcelRowValue;
      }
      // 匹配则获取此sheet下所有的row值
      List<List<String>> sheetValues = this.getSheetValue(sheet);
      if (!CollectionUtils.isEmpty(sheetValues)) {
        allExcelRowValue.addAll(sheetValues);
      }
    }
    return allExcelRowValue;
  }

  /**
   *
   * @param sheet
   */
  private List<List<String>> getSheetValue(Sheet sheet) {
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    logger.debug(String.format("第一行row是%s,最后一行row是%s,共计%s行数据", firstRowNum, lastRowNum,
        lastRowNum - firstRowNum + 1));
    List<List<String>> allSheetRowValue = new ArrayList<List<String>>();
    // 获取所有行数的值
    for (int i = 0; i <= lastRowNum; i++) {
      // 检验是否最后一行
      if (this.validationIsLastRow(sheet.getRow(i))) {
        break;
      }
      // 获取当前row的值
      List<String> rowValue = this.getRowValue(sheet.getRow(i));
      // 当前row的值存放到list中
      allSheetRowValue.add(rowValue);
    }
    return allSheetRowValue;
  }

  /**
   * 获取row的值
   * 
   * @param row
   * @return
   */
  public List<String> getRowValue(Row row) {
    List<String> tempList = new ArrayList<String>();
    for (int i = 0; i < row.getLastCellNum(); i++) {
      String cellValue = formatCellValue(row.getCell(i));
      tempList.add(cellValue);
    }
    return tempList;
  }


  /**
   * 检查是否是空行,如果是空行则判断读取到文件末尾 true = 是最后一行 false=不是最后一行
   *
   * @param row
   * @return
   */
  private boolean validationIsLastRow(Row row) {
    // 如果该行的起始单元格==截止单元格且该单元格的值是空,就认为是空行,读取到文件末尾
    if (row.getFirstCellNum() == row.getLastCellNum()
        && StringUtils.isEmpty(formatCellValue(row.getCell(row.getFirstCellNum())))) {
      return true;
    }
    return false;
  }


  /**
   * 将excel中cell中的值转换为string
   * 
   * @param cell
   * @return
   */
  private static String formatCellValue(Cell cell) {

    String cellValue = "";
    if (cell != null) {
      switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:
        case Cell.CELL_TYPE_FORMULA:
          if (DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cellValue = sdf.format(date);
          } else {
            cellValue = String.valueOf(cell.getNumericCellValue());
            if (cellValue.endsWith(".0")) {
              cellValue = cellValue.replace(".0", "");
            }
          }
          break;
        case Cell.CELL_TYPE_STRING:
          cellValue = cell.getRichStringCellValue().getString();
          break;
        case Cell.CELL_TYPE_BOOLEAN:
          boolean comment = cell.getBooleanCellValue();
          cellValue = comment ? "Y" : "N";
          break;
        default:
          cellValue = "";
      }
    }
    return cellValue;

  }


  public static void main(String[] args) throws IOException {
    // FileInputStream inputStream =
    // new FileInputStream(new File("C:\\Users\\杨堡\\Downloads\\电子发票导入模板.xls"));
    FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\杨堡\\Desktop\\11.xlsx"));

//    Workbook workbook = new HSSFWorkbook(inputStream);
    Workbook workbook = new XSSFWorkbook(inputStream);
    System.out.println("workbook's total numbers of sheets is : " + workbook.getNumberOfSheets());
    Sheet sheet = workbook.getSheetAt(0);
    System.out.println(sheet.getFirstRowNum());
    System.out.println(sheet.getLastRowNum());
    Row row = sheet.getRow(0);
    System.out.println(row.getFirstCellNum());
    System.out.println(row.getLastCellNum());
    System.out.println(formatCellValue(row.getCell(0)));
    System.out.println("*********************************************");
    String a = "";
    System.out.println(StringUtils.isEmpty(a));
    System.out.println("*********************************************");
    Row row1 = sheet.getRow(10);
    System.out.println(row1.getFirstCellNum()+"——"+row1.getLastCellNum());
    Row row2 = sheet.getRow(111);
    System.out.println(row2.getFirstCellNum()+"——"+row2.getLastCellNum());
  }
}
