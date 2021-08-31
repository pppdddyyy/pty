package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usian.pojo.ItemParam;
import com.usian.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: ItemParamAction
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 9:38
 */
@RestController
@RequestMapping("itemParam")
public class ItemParamAction {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public ItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){


        QueryWrapper<ItemParam> condition = new QueryWrapper<>();
        condition.eq("item_cat_id",itemCatId);

        List<ItemParam> list =
                itemParamService.list(condition);

        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;

    }
}
