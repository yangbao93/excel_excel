package com.yonyou.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 参数表
 * Created by yangbao on 2017/11/28.
 */
@Getter
@Setter
public class ConfigEntity {
  /**
   * 系统id
   */
  private Integer id;
  /**
   * 父id
   */
  private Integer pid;
  /**
   * type类型，是哪种解析类型
   */
  private Integer type;
  /**
   * hOrder 代表层级，0是第一级
   */
  private Integer level;
  /**
   * entityName 实体名称
   */
  private String entity_name;
  /**
   * entityUrl
   */
  private String entity_url;
  /**
   * 序列id
   */
  private Integer orderid;
  /**
   * 表头名称
   */
  private String btmc;
  /**
   * 实体对应属性名称
   */
  private String stmc;
  /**
   * 属性是否可空，Y-可以，N-不可
   */
  private boolean isnull;
  /**
   * 公司id
   */
  private String corpid;

}
