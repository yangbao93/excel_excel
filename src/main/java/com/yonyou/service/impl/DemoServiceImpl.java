package com.yonyou.service.impl;

import com.yonyou.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.service.DemoService;

/**
 * demo服务实现类
 * Created by yangbao on 2017/11/28.
 */
@Service
public class DemoServiceImpl implements DemoService{

  @Autowired
  private DemoDao demoDao;

  @Override
  public void say() {
    System.out.println("this is service");
  }

  @Override
  public void getInfo() {
    String info = demoDao.getInfo();
    System.out.println(info);
  }
}
