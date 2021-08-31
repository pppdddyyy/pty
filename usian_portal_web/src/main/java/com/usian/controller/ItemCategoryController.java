package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemCategoryController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/12 14:11
 */
@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {


    @Autowired
    private ItemFeign itemFeign;


    @RequestMapping("selectItemCategoryAll")
    public Result selectItemCategoryAll(){

        ItemCategoryAllVo data = itemFeign.selectItemCategoryAll();

        return Result.ok(data);

    }


}
