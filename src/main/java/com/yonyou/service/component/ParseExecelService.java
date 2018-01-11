package com.yonyou.service.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.service.ExcelService;
import com.yonyou.util.BusinessRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.entity.ConfigEntity;

/**
 * excel解析 Created by yangbao on 2017/11/28.
 */
@Component
public class ParseExecelService {

  @Autowired
  private ExcelService readExcelService;

  /**
   * 解析excel
   * 
   * @return
   */
  public <T> List<T> parseExcel(List<List<String>> excelValue, List<ConfigEntity> configEntityList)
      throws Exception {
    /**
     * 首先解析第一行，第一行的参数个数和顺序与数据库存放数据进行校验。 未开票导入解析第二行，商品档案解析第一行 这些都不考虑了
     *
     */
    if (CollectionUtils.isEmpty(excelValue)) {
      throw new BusinessRuntimeException("错误的excelValue值");
    }
    if (CollectionUtils.isEmpty(configEntityList)) {
      throw new BusinessRuntimeException("错误的configEntity值");
    }
    // 解析配置list中的层级问题 todo 层级查询的时候需要按照order排序
    Map<Integer, List<ConfigEntity>> configMap = this.configListToConfigMap(configEntityList);
    // 存放要转成返回的bean实体名称
    String beanPath = configMap.get(0).get(0).getEntity_url();
    // 存放整个excel解析出来的json todo level层级应该从0开始
    List<Map<Integer, List<JSONObject>>> allBeanList =
        new ArrayList<Map<Integer, List<JSONObject>>>();
    // 遍历每一个list
    for (int n = 0; n < excelValue.size(); n++) {
      // 判断该行数据是属于allBeanList中的哪一个jsonMap
      Map<Integer, List<JSONObject>> jsonMap =
          this.checkAndCreateNewBean(excelValue.get(n), configMap, allBeanList);
      // 获取excel中第n行的数据
      List<String> stringList = excelValue.get(n);
      // 解析configMap中每个level
      for (int i = 0; i < configMap.size(); i++) {
        // 存放每个层级解析出来json格式的bean
        JSONObject beanJson = new JSONObject();
        List<ConfigEntity> entityList = configMap.get(i);
        for (int j = 0; j < entityList.size(); j++) {
          // 获取每个配置项
          ConfigEntity tempEntity = entityList.get(j);
          // 获得配置项对应的excel格的值
          String value = stringList.get(tempEntity.getOrderid());
          beanJson.put(tempEntity.getStmc(), value);
        }
        // 如果不是空层级，且关键数据为空则抛出异常
        if (this.isNullJson(beanJson, entityList)) {
          continue;
        }
        // 获取jsonMap中的list,如果为空则生成list,存放beanJson到list中
        List<JSONObject> jsonObjects = jsonMap.get(i);
        if (CollectionUtils.isEmpty(jsonObjects)) {
          jsonObjects = new ArrayList<JSONObject>();
        }
        jsonObjects.add(beanJson);
        jsonMap.put(i, jsonObjects);
        // 处理父子层级关系
        if (i >= 1) {
          // 获取父层的所有list
          List<JSONObject> fatherLevelList = jsonMap.get(i - 1);
          // 获取父层的最后一个为当前beanjson的父亲
          JSONObject fatherJson = fatherLevelList.get(fatherLevelList.size() - 1);
          // 获取父层下的items，即当前beanJson所在的层级
          JSONArray items = fatherJson.getJSONArray("items");
          if (CollectionUtils.isEmpty(items)) {
            items = new JSONArray();
          }
          items.add(beanJson);
          fatherJson.put("items",items);
        }
      }
    }
    // 解析beanList转成对应的实体list
    List<T> returnCollection = this.jsonObjectToBean(beanPath, allBeanList);
    return returnCollection;
  }

  /**
   * 将json对象转化为javabean对象
   * 
   * @param beanPath
   * @param allBeanList
   * @param <T>
   * @return
   * @throws ClassNotFoundException
   */
  private <T> List<T> jsonObjectToBean(String beanPath,
      List<Map<Integer, List<JSONObject>>> allBeanList)
      throws ClassNotFoundException, IllegalAccessException, InstantiationException {
    List<T> returnCollection = new ArrayList<T>();
    for (int i = 0; i < allBeanList.size(); i++) {
      // 获取最顶层父类的jsonobject
      JSONObject sourceJson = allBeanList.get(i).get(0).get(0);
      // 获取顶层父类要转换的class
      Class<?> aClass = Class.forName(beanPath);
      Object originObject = aClass.newInstance();
      Object o = JSONObject.toJavaObject(sourceJson, originObject.getClass());
      returnCollection.add((T) o);
    }
    return returnCollection;
  }

  /**
   * 判断是否是空对象，先判断是不是全部都为空，全部都为空则是空对象，如果有配置不为空，但关键数据存在空，则抛出关键数据为空的异常
   * 
   * @param object
   * @param entityList
   * @return
   */
  private boolean isNullJson(JSONObject object, List<ConfigEntity> entityList) {
    // 是否是空层级
    boolean isEmptyLevel = true;
    // 是否存在关键值为空
    boolean importantItemIsNull = false;
    for (int i = 0; i < entityList.size(); i++) {
      if (!entityList.get(i).isIsnull()
          && StringUtils.isEmpty(object.get(entityList.get(i).getStmc()))) {
        importantItemIsNull = true;
      }
      if (!StringUtils.isEmpty(object.get(entityList.get(i).getStmc()))) {
        isEmptyLevel = false;
      }
    }
    // 如果是空层返回true
    if (isEmptyLevel) {
      return true;
    }
    if (!isEmptyLevel && importantItemIsNull) {
      throw new BusinessRuntimeException("必填字段:" + entityList.get(0).getBtmc() + "不可以为空！");
    }
    return false;
  }

  /**
   * 判断是否是新的一个实体，父节点存在数据则表示为新的bean实体
   * 
   * @param valueStrings
   * @param configMap
   * @param allBeanList
   */
  private Map<Integer, List<JSONObject>> checkAndCreateNewBean(List<String> valueStrings,
      Map<Integer, List<ConfigEntity>> configMap,
      List<Map<Integer, List<JSONObject>>> allBeanList) {
    List<ConfigEntity> entities = configMap.get(0);
    // 判斷是否是新的一个实体
    boolean newBean = false;
    for (ConfigEntity configEntity : configMap.get(0)) {
      if (!StringUtils.isEmpty(valueStrings.get(configEntity.getOrderid()))) {
        newBean = true;
      }
    }
    // 如果是新的实体,建立新的一个实体map
    if (newBean) {
      Map<Integer, List<JSONObject>> jsonMap = new HashMap<Integer, List<JSONObject>>();
      for (int i = 0; i < configMap.size(); i++) {
        jsonMap.put(i, new ArrayList<JSONObject>());
      }
      allBeanList.add(jsonMap);
    }
    return allBeanList.get(allBeanList.size() - 1);
  }

  /**
   * 将configList按照层级转换为configMap， 0-List<>，1-List<>，2-List<>
   *
   * @param configEntityList
   * @return
   */
  private Map<Integer, List<ConfigEntity>> configListToConfigMap(
      List<ConfigEntity> configEntityList) {
    // 返回的configMap
    Map<Integer, List<ConfigEntity>> configMap = new HashMap<Integer, List<ConfigEntity>>();
    for (ConfigEntity config : configEntityList) {
      // 获取层级
      Integer level = config.getLevel();
      // 获取层级对应的List，如果没有就新建
      List<ConfigEntity> levelList = configMap.get(level);
      if (CollectionUtils.isEmpty(levelList)) {
        levelList = new ArrayList<ConfigEntity>();
      }
      levelList.add(config);
      configMap.put(level, levelList);
    }
    return configMap;
  }


}
