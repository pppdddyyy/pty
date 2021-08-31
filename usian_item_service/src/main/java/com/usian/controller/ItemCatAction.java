package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usian.pojo.ItemCat;
import com.usian.service.ItemCatService;
import com.usian.util.RedisClient;
import com.usian.vo.ItemCategoryAllVo;
import com.usian.vo.ItemCategoryNodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ItemCatAction
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 8:52
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatAction {


    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private RedisClient redisClient;


    @RequestMapping("/selectItemCategoryAll")
    public ItemCategoryAllVo selectItemCategoryAll(){

        //1. 查询缓存中是否有数据
        ItemCategoryAllVo redisData  = (ItemCategoryAllVo) redisClient.get("USIAN_INDEX_CATEGORY_ALL");

        // 如果数据就直接返回
        if(redisData!=null){
            return redisData;
        }


        //没有数据，触发数据库的访问，查询到的结果，放入到redis缓存中


        //1.从数据库中查询全部的类目，平铺的方式
//        List<ItemCat> itemCats = itemCatService.list();


        //2. 改变成树形
        //  parent_id = 0;  select * from tb_item_cat where parent_id = ?
        //2.1 先找到1级，根据1级2级    。。。。   。。。。

        //查询询全部的类目，树形

        List oneItemCat =  queryData(0L);
        ItemCategoryAllVo itemCategoryAllVo = new ItemCategoryAllVo();
        itemCategoryAllVo.setData(oneItemCat);

        redisClient.set("USIAN_INDEX_CATEGORY_ALL",itemCategoryAllVo);


        return itemCategoryAllVo;

    }

    /**
     * 根据父id查询对应的子节节点集合
     * @param parentId
     * @return
     */
    public List queryData(Long parentId){ // 0

        QueryWrapper<ItemCat> condition = new QueryWrapper<>();
            condition.eq("parent_id",parentId).eq("status","1");
        List<ItemCat> childrens = itemCatService.list(condition);//一级类目

        // ItemCat ----> ItemCategoryNodeVo/string
        List  result = new ArrayList();
        for (ItemCat itemCat:childrens){

            if(itemCat.getIsParent()){// 是父节点
                ItemCategoryNodeVo node = new ItemCategoryNodeVo();
                node.setN(itemCat.getName());
                node.setI(queryData(itemCat.getId()));//  select * from tb_item_cat where parent_id = 1
                result.add(node);

            }else{//不是父节点
                result.add(itemCat.getName());
            }
        }

        return result;

    }



    @RequestMapping("/selectItemCategoryByParentId")
    public List<ItemCat> selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id){

        // select * from tb_item_cat where  parentId = ?
        //查询条件
        QueryWrapper<ItemCat> condition = new QueryWrapper<>();
        condition.eq("parent_id",id).eq("status","1");
        return itemCatService.list(condition);


    }
}
