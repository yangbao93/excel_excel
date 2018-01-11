/**
 *
 */
package com.yonyou.util;

/**
 * @author wangweir
 */
public class BusinessRuntimeException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * 错误编码
   */
  private String code;


  public BusinessRuntimeException(String code, String msg) {
    super(msg);
    this.code = code;
  }

  public BusinessRuntimeException(String code, String msg, Throwable cause) {
    super(msg, cause);
    this.code = code;
  }

  public BusinessRuntimeException(String message) {
    super(message);
  }

  public BusinessRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

}
