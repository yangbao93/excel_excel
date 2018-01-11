package com.yonyou.controller;

import com.yonyou.entity.ConfigEntity;
import com.yonyou.service.ConfigurationService;
import com.yonyou.util.BusinessRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处理excel参数设置
 * Created by yangbao on 2017/12/5.
 */
@RestController
@RequestMapping("/configentity")
public class ConfigEntityController {

  private static Logger logger = LoggerFactory.getLogger(ConfigEntityController.class);

  @Autowired
  private ConfigurationService configurationService;

  @RequestMapping(value = "save",method = RequestMethod.POST)
  public void saveConfigEntity(@RequestBody List<ConfigEntity> entityList) {
    logger.debug("调用excel配置保存");
    //检验非空
    if (CollectionUtils.isEmpty(entityList)){
      logger.debug("传入的参数为空");
      throw new BusinessRuntimeException("传递参数不能为空");
    }
    configurationService.saveConfigEntity(entityList);
    logger.debug("调用excel配置保存成功");
    return;
  }

  @RequestMapping(value = "select", method = RequestMethod.POST)
  public List<ConfigEntity> selecConfigEntity(@RequestParam String corpid,@RequestParam int type) {

    logger.debug("调用获取excel配置");
    List<ConfigEntity> entities = configurationService.selectConfigEntityList(corpid, type);
    logger.debug("调用获取excel配置成功");
    return entities;
  }

  @RequestMapping(value = "update",method = RequestMethod.POST)
  public void updateConfigEntity(@RequestBody List<ConfigEntity> entityList) {
    logger.debug("调用excel配置更新");
    logger.debug("删除旧excel配置");
    configurationService.updateConfigEntity(entityList);
  }

  @RequestMapping(value = "default",method = RequestMethod.POST)
  public List<ConfigEntity> selectDefault(@RequestParam int type) {
    List<ConfigEntity> configEntities = configurationService.selectConfigEntityList("0", type);
    return configEntities;
  }
}
