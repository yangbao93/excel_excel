package com.yonyou.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 检查是否火狐浏览器 Created by yangbao on 2017/12/22.
 */
public class CheckFireFoxUtil {

  /**
   * 检查是否火狐浏览器
   * 
   * @param request
   * @return
   */
  public static boolean isFireFox(HttpServletRequest request) {
    String userAgent = request.getHeader("user-agent");

    if (StringUtils.isEmpty(userAgent)) {
      return false;
    }

    String user = userAgent.toLowerCase();
    if (user.contains("firefox")) {
      return true;
    }
    return false;
  }
}
