package com.usian.vo;

import com.usian.pojo.Item;

/**
 * @Title: ItemVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 15:55
 * 用户新增是提交的数据类型
 */
public class ItemVO extends Item {

    private String desc;
    private String itemParams;
    private String catName;//分类名称

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }
}
