package com.yonyou.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.yonyou.entity.ConfigEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigEntityDao {

  /**
   * 添加父表数据
   * 
   * @param configEntity
   * @return
   */
  int saveFatherConfigEntity(ConfigEntity configEntity);

  /**
   * 添加子表数据
   * 
   * @param configEntities
   */
  void saveSonConfigEntity(@Param("list") List<ConfigEntity> configEntities);

  /**
   * 查询公司下,类型下的配置
   * 
   * @param corpId
   * @param type
   * @return
   */
  List<ConfigEntity> selectConfigEntityList(@Param("corpId") String corpId,
      @Param("type") int type);
  
  /**
   * 根据id删除config和config_b中的数据
   * 
   * @param id
   */
  void deleteConfigEntityById(@Param("id") int id);

  /**
   * 根据type和corpid 查询id
   * 
   * @param corpid
   * @param type
   */
  int selectIdByCorpidAndType(@Param("corpid") String corpid, @Param("type") int type);
}
