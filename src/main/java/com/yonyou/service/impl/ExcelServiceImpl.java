package com.yonyou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.entity.ConfigEntity;
import com.yonyou.entity.ExcelExportEntity;
import com.yonyou.service.ConfigurationService;
import com.yonyou.service.ExcelService;

import com.yonyou.service.component.ParseExecelService;
import com.yonyou.service.component.ReadExcelValueService;
import com.yonyou.util.BusinessRuntimeException;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 读取excel文档
 * 
 * @author yangbao
 */
@Service
public class ExcelServiceImpl implements ExcelService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static String SPLIT = "\\.";

  private final int MAIN_SIZE = 1;

  @Autowired
  private ReadExcelValueService readExcelValueService;

  @Autowired
  private ParseExecelService parseExecelService;

  @Autowired
  private ConfigurationService configurationService;

  @Override
  public List<Object> readExcelValue(InputStream inputStream) {
    logger.debug("解析excel开始");
    logger.debug("调用ReadExcelValueService开始,解析excel中每一行的值");
    // 读取所有的
    List<List<String>> init = readExcelValueService.init(inputStream);
    logger.debug("数据库中获取模板的配置");
    // 读取配置文件中的config todo 获取configList
    List<ConfigEntity> configList = configurationService.selectConfigEntityList("1", 1);
    // 判断标题行格式是否正确 todo 由于存在两种格式的模板,需要验证两行,如果第一行成功则无需验证第二行,第一行不成功则验证第二行
    logger.debug("判断excel中标题内容是否和配标文件一致");
    int startIndex = 0;
    if (!this.validationHeadWithConfigList(init.get(startIndex), configList)) {
      ++startIndex;
      if (!this.validationHeadWithConfigList(init.get(startIndex), configList)) {
        throw new BusinessRuntimeException("标题行和配置文件名称不相符");
      }
    }
    logger.debug("获取标题行以下的待解析的内容");
    // 截取标题行以下的所有内容
    List<List<String>> subList = init.subList(++startIndex, init.size());
    // 转换成javabean
    try {
      logger.debug("转化为javabean开始");
      List<Object> objects = parseExecelService.parseExcel(subList, configList);
      logger.debug("转化javabean完成,javabean:" + objects);
      return objects;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("解析出错,程序已经终止", e.getMessage());
      throw new BusinessRuntimeException("excel转换javabean解析失败");
    }

  }

  @Override
  public HSSFWorkbook exportExcelTemplate(String corpid, int type) {
    // 查询该公司该类型下的的配置项目信息
    List<ConfigEntity> entityList = configurationService.selectConfigEntityList(corpid, type);
    // 创建excel工作簿
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 创建excel页
    HSSFSheet sheet = workbook.createSheet();
    // 创建第一行,并填入注意事项
    HSSFRow row0 = sheet.createRow(0);
    row0.createCell(0).setCellValue("*红色部分是必填项");
    // 创建第二行,第二行填入配置文件中的项目,必填项设置特殊格式
    HSSFRow row1 = sheet.createRow(1);
    // 获取必填项目单元格格式
    HSSFCellStyle cellStyle = this.createCellStyle(workbook);
    for (int i = 0; i < entityList.size(); i++) {
      HSSFCell cell = row1.createCell(i);
      // 如果是必填项目,设置单元格格式文件
      if (!entityList.get(i).isIsnull()) {
        cell.setCellStyle(cellStyle);
      }
      cell.setCellValue(entityList.get(i).getBtmc());
    }
    return workbook;

  }

  @Override
  public HSSFWorkbook exportExcelData(ExcelExportEntity exportEntity) {
    logger.debug("开始执行导出excel的数据");
    // 获取标题和属性名信息
    Map<String, String> headAndAttributes = exportEntity.getHeadAndAttributes();
    // 校验当前数据的完整性
    logger.debug("校验标题，数据是否为空");
    if (CollectionUtils.isEmpty(exportEntity.getDatas())
        || CollectionUtils.isEmpty(headAndAttributes)) {
      logger.error("关键数据为空");
      throw new BusinessRuntimeException("关键数据（行名称，导出数据）不能为空");
    }
    // 创建excel工作簿
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 创建excel页
    HSSFSheet sheet = workbook.createSheet();
    int rowSize = 0;
    // 查看数据是否有条件信息
    logger.debug("查看是否有条件信息，如果有则将条件信息写入sheet中");
    rowSize = this.buildConditionsInfo(exportEntity, sheet, rowSize);
    // 查看数据中的标题和属性名信息
    logger.debug("创建标题信息，取出对应的实体属性名称");
    List<String> attributesList = this.buildHeadInfo(headAndAttributes, rowSize, sheet);
    // 处理完标题后将rowSize++
    rowSize++;
    // 填充数据信息
    List<Object> datas = exportEntity.getDatas();
    for (int i = 0; i < datas.size(); i++) {
      // 创建行元素
      HSSFRow everyDataRow = sheet.createRow(rowSize++);
      String jsonString = JSON.toJSONString(datas.get(i));
      JSONObject jsonObject = JSONObject.parseObject(jsonString);
      // 存放子集序列和名称
      Map<Integer, String> itemOrderAndName = new HashMap<>(16);
      // 遍历所有的属性名称
      for (int j = 0; j < attributesList.size(); j++) {
        String attributesName = attributesList.get(j);
        String[] names = attributesName.split(SPLIT);
        if (names.length > MAIN_SIZE) {
          // 如果是list
          itemOrderAndName.put(j, attributesName);
          continue;
        } else {
          // 如果不是list
          String value = jsonObject.getString(attributesName);
          if (!StringUtils.isEmpty(value)) {
            everyDataRow.createCell(j).setCellValue(value);
          }
        }
      }
      // 处理子集问题
      if (!CollectionUtils.isEmpty(itemOrderAndName)) {
        rowSize = this.buildItemsInfo(sheet, jsonObject, itemOrderAndName, rowSize);
      }
    }
    return workbook;
  }

  /**
   * 处理子集问题，目前只支持两级结构
   * 
   * @param sheet 工作表
   * @param jsonObject 处理的对象（从中找到子集集合）
   * @param itemOrderAndName 子集对象的排序号和名称
   * @param rowSize 行号
   * @return 行号编码
   */
  private int buildItemsInfo(HSSFSheet sheet, JSONObject jsonObject,
      Map<Integer, String> itemOrderAndName, int rowSize) {
    List<Integer> keyList = new ArrayList<>();
    keyList.addAll(itemOrderAndName.keySet());
    // 获取一个子集的key
    Integer tempKey = keyList.get(0);
    // 获取子集的名称
    String itemName = itemOrderAndName.get(tempKey).split(SPLIT)[0];
    // 获取子集内容
    JSONArray itemArray = jsonObject.getJSONArray(itemName);
    if (CollectionUtils.isEmpty(itemArray)) {
      // 如果子集为空，返回
        return rowSize;
    }
    for (int i = 0; i < itemArray.size(); i++) {
      // 获取子集List中的元素
      JSONObject itemObject = (JSONObject) itemArray.get(i);
      HSSFRow itemRow = null;
      if (i == 0) {
        // 处理第一个子数据，第一行数据应该是和主数据一致
        itemRow = sheet.getRow(rowSize - 1);
      } else {
        // 非第一个子数据应当另起一行
        itemRow = sheet.createRow(rowSize++);
      }
      for (Integer key : keyList) {
        // 获取这个key对应的属性名称
        String valueName = itemOrderAndName.get(key);
        // 属性名称进行分割
        String[] strings = valueName.split(SPLIT);
        // 获取子属性中的属性名
        String attributeName = strings[strings.length - 1];
        // 赋值到单元格中
        itemRow.createCell(key).setCellValue(itemObject.getString(attributeName));
      }
    }
    return rowSize;
  }

  /**
   * 创建标题行信息，解析出每个标题对应的属性名称
   * 
   * @param headAndAttributes 标题行/属性名 map
   * @param rowSize 行号
   * @param sheet 工作表
   * @return 标题对应的属性名List
   */
  private List<String> buildHeadInfo(Map<String, String> headAndAttributes, int rowSize,
      HSSFSheet sheet) {
    Set<String> keySet = headAndAttributes.keySet();
    List<String> attributesList = new LinkedList<>();
    int tempSize = 0;
    HSSFRow headRow = sheet.createRow(rowSize);
    for (String key : keySet) {
      // 存放标题信息
      headRow.createCell(tempSize++).setCellValue(key);
      // 存放属性信息
      if (StringUtils.isEmpty(headAndAttributes.get(key))) {
        throw new BusinessRuntimeException("标题对应的属性名为空");
      }
      attributesList.add(headAndAttributes.get(key));
    }
    return attributesList;
  }

  /**
   * 创建条件信息
   *
   * @param exportEntity 导出实体
   * @param sheet 工作表
   * @param rowSize 行号
   * @return 返回最后的行号
   */
  private int buildConditionsInfo(ExcelExportEntity exportEntity, HSSFSheet sheet, int rowSize) {
    Map<String, String> conditions = exportEntity.getConditions();
    if (!CollectionUtils.isEmpty(conditions)) {
      // 有条件信息，从头开始存放条件信息
      Set<String> conditionKey = conditions.keySet();
      for (String key : conditionKey) {
        HSSFRow row = sheet.createRow(rowSize++);
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(conditions.get(key));
      }
    }
    return rowSize;
  }

  /**
   * 设置必输项单元格格式,字体加粗,红色
   *
   * @param workbook 工作簿
   * @return 单元格样式
   */
  private HSSFCellStyle createCellStyle(HSSFWorkbook workbook) {
    // 创建样式
    HSSFCellStyle style = workbook.createCellStyle();
    // 创建字体
    Font font = workbook.createFont();
    // 设置字体红色
    font.setColor(HSSFColor.RED.index);
    style.setFont(font);
    return style;
  }

  /**
   * 检查标题是否和配置文件相关 true=代表匹配,false=不匹配 todo 返回应该附带标题的行数
   */
  private boolean validationHeadWithConfigList(List<String> allValue,
      List<ConfigEntity> configList) {
    return this.validationRow(allValue, configList);
  }

  /**
   * 检验标题行和配置文件是不是一一对应的(顺序和名称都一致,表示通过验证)
   *
   * @param row 行
   * @param configList 配置信息list
   * @return 成功或失败
   */
  private boolean validationRow(List<String> row, List<ConfigEntity> configList) {
    for (int i = 0; i < row.size(); i++) {
      ConfigEntity configEntity = configList.get(i);
      if (!configEntity.getBtmc().equals(row.get(i))) {
        return false;
      }
    }
    return true;
  }

}
