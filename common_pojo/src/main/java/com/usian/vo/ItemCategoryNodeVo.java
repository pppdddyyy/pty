package com.usian.vo;

import java.util.List;

/**
 * @Title: ItemCategoryNodeVo
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/12 14:24
 */
public class ItemCategoryNodeVo {

    private String n;//类目名称

    private List i;// 子类目的集合


    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public List getI() {
        return i;
    }

    public void setI(List i) {
        this.i = i;
    }
}
