package com.yonyou.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 导出数据时的模板 Created by yangbao on 2018/1/8.
 * 
 * @author yangbao
 */
@Getter
@Setter
public class ExcelExportEntity implements Serializable {

  private static final long serialVersionUID = -5733664052899026996L;
  /**
   * 导出条件
   */
  private LinkedHashMap<String, String> conditions;
  /**
   * 标题和对应的属性
   */
  private LinkedHashMap<String, String> headAndAttributes;
  /**
   * 导出的实体List
   */
  private LinkedList<Object> datas;
}
