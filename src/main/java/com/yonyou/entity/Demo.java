package com.yonyou.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * demo实体
 * Created by yangbao on 2017/11/28.
 */
@Getter
@Setter
public class Demo {

  private int id;

  private String name;

  private String value;

  private ConfigEntity part;

  private List<ExcelBean> items;

}
