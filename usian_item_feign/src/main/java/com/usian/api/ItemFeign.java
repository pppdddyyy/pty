package com.usian.api;

import com.usian.pojo.Item;
import com.usian.pojo.ItemCat;
import com.usian.pojo.ItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryAllVo;
import com.usian.vo.ItemResultVO;
import com.usian.vo.ItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ItemFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 9:41
 */
@FeignClient("usian-item-service")
public interface ItemFeign {

    /**
     * 查询所有的商品类目数据，且数据结构为树形结构
     * @return
     */
    @RequestMapping("/itemCat/selectItemCategoryAll")
    public ItemCategoryAllVo selectItemCategoryAll();

    /**
     * 获取某个商品对象的详情信息
     * @param itemId
     * @return
     */
    @RequestMapping("/item/preUpdateItem")
    public ItemResultVO preUpdateItem(@RequestParam("itemId") Long itemId);
    /**
     * 新增商品接口
     * @param itemVO
     */

    @RequestMapping("/item/insertTbItem")
    public void insertTbItem(@RequestBody ItemVO itemVO);

    /**
     * 根据类目ID，查询对应的规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping("/itemParam/selectItemParamByItemCatId/{itemCatId}")
    public ItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);


    /**
     * 根据父类目ID，查询下面所有的子类目
     * @param id
     * @return
     */
    @RequestMapping("/itemCat/selectItemCategoryByParentId")
    public  List<ItemCat> selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id);

    @GetMapping("/item/selectTbItemAllByPage")
    public PageResult<Item> selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name="rows") Integer rows);



        @RequestMapping("/item/") // 跟下游路径保持一致
    public List<Item> queryAll();

        @RequestMapping("/item/selectItemInfo")
    Item selectItemById(@RequestParam("itemId") Long itemId);
}
