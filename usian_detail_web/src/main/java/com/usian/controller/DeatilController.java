package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: DeatilController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/19 10:59
 */
@RestController
@RequestMapping("/frontend/detail")
public class DeatilController {

    @Autowired
    private ItemFeign itemFeign;

    /**
     * 查询商品基本详情信息
     * @param itemId
     * @return
     */
    @RequestMapping("selectItemInfo")
    public Result selectItemInfo(@RequestParam("itemId") Long itemId ){

         Item item = itemFeign.selectItemById(itemId);
         return Result.ok(item);
    }


}
