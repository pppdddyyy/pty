package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemResultVO;
import com.usian.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ItemController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 9:37
 */
@RestController
@RequestMapping("/backend/item")
public class ItemController {

    @Autowired
    private ItemFeign itemFeign;



    @RequestMapping("preUpdateItem")
    public Result preUpdateItem(@RequestParam("itemId") Long itemId){
      ItemResultVO itemResultVO = itemFeign.preUpdateItem(itemId);

      return Result.ok(itemResultVO);


    }

    @RequestMapping("insertTbItem")
    public Result insertTbItem( ItemVO itemVO){
        try {
            itemFeign.insertTbItem(itemVO);
        } catch (Exception e) {
            Result.error(e.getMessage());
        }

        return Result.ok();
    }

    @GetMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam(name="rows",defaultValue = "2") Integer rows){

        PageResult pageResult = null;
        try {
            pageResult = itemFeign.selectTbItemAllByPage(page,rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
           return Result.error(e.getMessage());
        }


    }







    /**
     *   前台  item/queryAll
     *     item-web
     *
     *      item/
     *      item-servie
     *
     *
     * @return
     */
    @RequestMapping("/queryAll")
    public Result queryAll(){
      List<Item> data =  itemFeign.queryAll();

      return Result.ok(data);

    }
}
