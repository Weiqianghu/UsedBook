package com.weiqianghu.usedbook.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class ProvinceModel implements Serializable{
    private String name;
    private List<CityModel> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityModel> getCity() {
        return city;
    }

    public void setCity(List<CityModel> city) {
        this.city = city;
    }
}
