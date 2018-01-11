package com.yonyou.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>InvoiceCollection[invoice_collection]数据持久化对象</b>
 * 
 * @author chenbiao
 * @date 2016-08-23 19:53:10
 */
public class InvoiceCollection implements Serializable {


  private static final long serialVersionUID = 1L;

  private Integer id;

  private String corpId;

  private Integer orgId;

  private Integer pk_invoice;

  private Integer srcType;

  private String submitter;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date submitDate;


  private String purchaser;


  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date createdate;

  private String vnote;

  private Date ts;

  private String fpHm;

  private String fpDm;

  private String needVerify;

  private String accountUser;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date accountTime;

  private String accountNote;

  // ****VO字段******

  private String kprq;
  private String fplx;
  private String kplx;
  private String xsfMc;
  private BigDecimal jshj;

  private String gmfMc;
  private String gmfDzdh;
  private String gmfNsrsbh;
  private String xsfNsrsbh;
  private String xsfDzdh;
  private BigDecimal hjje;
  private BigDecimal hjse;
  private String bz;
  private Integer purchaserstatus;
  private String filepath;
  /**
   * 判断filePath是否存在值
   */
  private boolean hasattache;
  private Integer verifyStatus;
  private BigDecimal totalOutMny;
  // ****VO字段end******
  
  //增加凭证号字段
  private String voucherid;
  
  public String getVoucherid() {
    return voucherid;
  }

  public void setVoucherid(String voucherid) {
    this.voucherid = voucherid;
  }

  public Integer getSrcType() {
	return srcType;
}

public String getFpHm() {
	return fpHm;
}

public void setFpHm(String fpHm) {
	this.fpHm = fpHm;
}

public String getFpDm() {
	return fpDm;
}

public void setFpDm(String fpDm) {
	this.fpDm = fpDm;
}

public String getKprq() {
	return kprq;
}

public void setKprq(String kprq) {
	this.kprq = kprq;
}

public String getFplx() {
	return fplx;
}

public void setFplx(String fplx) {
	this.fplx = fplx;
}

public String getKplx() {
	return kplx;
}

public void setKplx(String kplx) {
	this.kplx = kplx;
}

public String getXsfMc() {
	return xsfMc;
}

public void setXsfMc(String xsfMc) {
	this.xsfMc = xsfMc;
}

public BigDecimal getJshj() {
	return jshj;
}

public void setJshj(BigDecimal jshj) {
	this.jshj = jshj;
}

public void setSrcType(Integer srcType) {
	this.srcType = srcType;
}

public String getSubmitter() {
	return submitter;
}

public void setSubmitter(String submitter) {
	this.submitter = submitter;
}

public Date getSubmitDate() {
	return submitDate;
}

public void setSubmitDate(Date submitDate) {
	this.submitDate = submitDate;
}

public String getPurchaser() {
	return purchaser;
}

public void setPurchaser(String purchaser) {
	this.purchaser = purchaser;
}

public Date getCreatedate() {
	return createdate;
}

public void setCreatedate(Date createdate) {
	this.createdate = createdate;
}

public String getVnote() {
	return vnote;
}

public void setVnote(String vnote) {
	this.vnote = vnote;
}

public Date getTs() {
	return ts;
}

public void setTs(Date ts) {
	this.ts = ts;
}

/**
   * 主键
   * 
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * 公司id
   * 
   * @return corpId
   */
  public String getCorpId() {
    return corpId;
  }

  /**
   * 组织id
   * 
   * @return orgId
   */
  public Integer getOrgId() {
    return orgId;
  }

  /**
   * 发票索引号
   * 
   * @return pk_invoice
   */
  public Integer getPk_invoice() {
    return pk_invoice;
  }


  /**
   * 主键
   * 
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 公司id
   * 
   * @param corpId
   */
  public void setCorpId(String corpId) {
    this.corpId = corpId;
  }

  /**
   * 组织id
   * 
   * @param orgId
   */
  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  /**
   * 发票索引号
   * 
   * @param pk_invoice
   */
  public void setPk_invoice(Integer pk_invoice) {
    this.pk_invoice = pk_invoice;
  }

  public String getGmfMc() {
    return gmfMc;
  }

  public void setGmfMc(String gmfMc) {
    this.gmfMc = gmfMc;
  }

  public String getGmfDzdh() {
    return gmfDzdh;
  }

  public void setGmfDzdh(String gmfDzdh) {
    this.gmfDzdh = gmfDzdh;
  }

  public String getGmfNsrsbh() {
    return gmfNsrsbh;
  }

  public void setGmfNsrsbh(String gmfNsrsbh) {
    this.gmfNsrsbh = gmfNsrsbh;
  }

  public String getXsfNsrsbh() {
    return xsfNsrsbh;
  }

  public void setXsfNsrsbh(String xsfNsrsbh) {
    this.xsfNsrsbh = xsfNsrsbh;
  }

  public String getXsfDzdh() {
    return xsfDzdh;
  }

  public void setXsfDzdh(String xsfDzdh) {
    this.xsfDzdh = xsfDzdh;
  }

  public BigDecimal getHjje() {
    return hjje;
  }

  public void setHjje(BigDecimal hjje) {
    this.hjje = hjje;
  }

  public BigDecimal getHjse() {
    return hjse;
  }

  public void setHjse(BigDecimal hjse) {
    this.hjse = hjse;
  }

  public String getBz() {
    return bz;
  }

  public void setBz(String bz) {
    this.bz = bz;
  }


  public Integer getPurchaserstatus() {
    return purchaserstatus;
  }

  public void setPurchaserstatus(Integer purchaserstatus) {
    this.purchaserstatus = purchaserstatus;
  }

  public Integer getVerifyStatus() {
    return verifyStatus;
  }

  public void setVerifyStatus(Integer verifyStatus) {
    this.verifyStatus = verifyStatus;
  }

  public BigDecimal getTotalOutMny() {
    return totalOutMny;
  }

  public void setTotalOutMny(BigDecimal totalOutMny) {
    this.totalOutMny = totalOutMny;
  }

  public String getNeedVerify() {
    return needVerify;
  }

  public void setNeedVerify(String needVerify) {
    this.needVerify = needVerify;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public boolean isHasattache() {
    return false;
  }

  public void setHasattache(boolean hasattache) {
    this.hasattache = hasattache;
  }

  public String getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(String accountUser) {
    this.accountUser = accountUser;
  }

  public Date getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(Date accountTime) {
    this.accountTime = accountTime;
  }

  public String getAccountNote() {
    return accountNote;
  }

  public void setAccountNote(String accountNote) {
    this.accountNote = accountNote;
  }

}
