package com.weiqianghu.usedbook.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class CityModel implements Serializable{
    private String name;
    private List<String> area;

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
