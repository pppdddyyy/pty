package com.usian.vo;

import com.usian.pojo.Item;

/**
 * @Title: ItemResultVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 16:30
 */
public class ItemResultVO {

    private String itemCat;//商品类目？？

    private Item item;//商品的基本信息对象

    private String itemDesc;//商品的描述

    private String itemParamItem;//规格参数值

    public String getItemCat() {
        return itemCat;
    }

    public void setItemCat(String itemCat) {
        this.itemCat = itemCat;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemParamItem() {
        return itemParamItem;
    }

    public void setItemParamItem(String itemParamItem) {
        this.itemParamItem = itemParamItem;
    }
}
