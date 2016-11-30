package com.chu.model;

import java.util.Date;
import java.io.Serializable;
/**
 * Created by P70 on 2016/11/15.
 */
public class User implements Serializable{
    private int id;
    private String name;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", createTime=" + date + "]";
    }
}
