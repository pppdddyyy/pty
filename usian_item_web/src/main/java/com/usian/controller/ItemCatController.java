package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.ItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: ItemCatController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 8:45
 */
@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCatController {


    @Autowired
    private ItemFeign itemFeign;

    /**
     *
     * @param id  类目父ID
     * @return
     */
    @RequestMapping("selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id){

      List<ItemCat> childrens =  itemFeign.selectItemCategoryByParentId(id);

      return Result.ok(childrens);
    }

}
