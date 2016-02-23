package com.weiqianghu.usedbook.model.entity;

import java.util.List;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class AddressModel {
    private List<ProvinceModel> provinceList;

    public List<ProvinceModel> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceModel> provinceList) {
        this.provinceList = provinceList;
    }
}
