package com.yonyou.entity;

import java.util.Date;
import java.util.List;

/**
 * 用于测试读取和解析
 * Created by yangbao on 2017/12/21.
 */
public class Student {

    private String name;

    private int age;

    private Date birthday;

    private List<ClassInfo> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<ClassInfo> getItems() {
        return items;
    }

    public void setItems(List<ClassInfo> items) {
        this.items = items;
    }
}
