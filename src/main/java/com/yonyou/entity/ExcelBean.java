package com.yonyou.entity;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.io.Serializable;

public class ExcelBean implements Serializable {

  private static final long serialVersionUID = -3350134648098572725L;
  private InputStream is;
  private Workbook wb;
  private Sheet[] sheets;
  private Sheet sheet;
  private Row row;
  private int sheetNum;
  private int rowNum;
  private int colNum;

  public InputStream getIs() {
    return is;
  }

  public Workbook getWb() {
    return wb;
  }

  public Sheet[] getSheets() {
    return sheets;
  }

  public Sheet getSheet() {
    return sheet;
  }

  public Row getRow() {
    return row;
  }

  public int getSheetNum() {
    return sheetNum;
  }

  public int getRowNum() {
    return rowNum;
  }

  public int getColNum() {
    return colNum;
  }

  public void setIs(InputStream is) {
    this.is = is;
  }

  public void setWb(Workbook wb) {
    this.wb = wb;
  }

  public void setSheets(Sheet[] sheets) {
    this.sheets = sheets;
  }

  public void setSheet(Sheet sheet) {
    this.sheet = sheet;
  }

  public void setRow(Row row) {
    this.row = row;
  }

  public void setSheetNum(int sheetNum) {
    this.sheetNum = sheetNum;
  }

  public void setRowNum(int rowNum) {
    this.rowNum = rowNum;
  }

  public void setColNum(int colNum) {
    this.colNum = colNum;
  }

}
