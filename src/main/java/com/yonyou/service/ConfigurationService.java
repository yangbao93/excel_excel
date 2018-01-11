package com.yonyou.service;

import com.yonyou.entity.ConfigEntity;

import java.util.List;

/**
 * 配置参数服务 Created by yangbao on 2017/11/28.
 */
public interface ConfigurationService {

  /**
   * 批量添加配置信息
   * 
   * @param configEntities
   */
  void saveConfigEntity(List<ConfigEntity> configEntities);

  /**
   * 获取配置信息
   * 
   * @param corpId
   * @param type
   * @return
   */
  List<ConfigEntity> selectConfigEntityList(String corpId, int type);

  /**
   * 根据corpid和type更新该组织的excel配置信息
   */
  void updateConfigEntity(List<ConfigEntity> entityList);
}
