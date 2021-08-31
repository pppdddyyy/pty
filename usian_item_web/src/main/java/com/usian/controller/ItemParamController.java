package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.ItemParam;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemParamController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/9 9:32
 */
@RestController
@RequestMapping("/backend/itemParam/")
public class ItemParamController {

    @Autowired
    private ItemFeign itemFeign;
    /**
     * 根据商品类目ID，查询对应的规格参数模板（某种商品类型特有的属性）
     * @param itemCatId
     * @return
     * 浏览器 --Resutl- WEB层 --有效数据-- service服务层
     */
    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){

      ItemParam itemParam= itemFeign.selectItemParamByItemCatId(itemCatId);

      return  Result.ok(itemParam);
    }
}
