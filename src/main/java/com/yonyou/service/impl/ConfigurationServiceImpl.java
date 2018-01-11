package com.yonyou.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yonyou.dao.ConfigEntityDao;
import com.yonyou.entity.ConfigEntity;
import com.yonyou.service.ConfigurationService;

/**
 * 配置参数服务实现类 Created by yangbao on 2017/11/28.
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

  private static Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

  private static final String SYSTEM_CORPID = "0";

  @Autowired
  private ConfigEntityDao configEntityDao;

  @Override
  @Transactional
  public void saveConfigEntity(List<ConfigEntity> configEntities) {
    logger.debug("添加父表数据");
    // 添加父表数据
    ConfigEntity configEntity = configEntities.get(0);
    configEntityDao.saveFatherConfigEntity(configEntity);
    logger.debug(String.format("父表数据添加完成,id为%s", configEntity.getId()));
    // 添加pid信息
    for (int i = 0; i < configEntities.size(); i++) {
      configEntities.get(i).setPid(configEntity.getId());
    }
    logger.debug("添加子表数据");
    // 添加子表数据
    configEntityDao.saveSonConfigEntity(configEntities);
    logger.debug("excel配置数据添加完成");
  }

  @Override
  public List<ConfigEntity> selectConfigEntityList(String corpId, int type) {
    // 查询用户配置的excel配置文件信息
    List<ConfigEntity> resultList = configEntityDao.selectConfigEntityList(corpId, type);
    // 如果用户没有进行过配置,则将系统配置返回
    if (CollectionUtils.isEmpty(resultList)) {
      List<ConfigEntity> systemConfigs =
          configEntityDao.selectConfigEntityList(SYSTEM_CORPID, type);
      return systemConfigs;
    }
    return resultList;
  }

  @Override
  @Transactional
  public void updateConfigEntity(List<ConfigEntity> entityList) {
    // 先根据corpid和type查询出原来的数据id;根据id删除原来的配置信息;插入新的配置信息
    logger.debug("更新excel配置数据");
    logger.debug("根据corpid和type删除excel配置信息");
    ConfigEntity entity = entityList.get(0);
    int id = configEntityDao.selectIdByCorpidAndType(entity.getCorpid(), entity.getType());
    logger.debug(String.format("根据corpid[%s],type[%s]查询到的配置id为[%s]", entity.getCorpid(),
        entity.getType(), id));
    logger.debug(String.format("根据id[%s]删除数据", id));
    configEntityDao.deleteConfigEntityById(id);
    logger.debug("数据删除完成,进行插入新的配置信息");
    this.saveConfigEntity(entityList);
    logger.debug("数据插入完成,更新excel配置信息完成");
  }

}
