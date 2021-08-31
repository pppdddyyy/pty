package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usian.pojo.Item;
import com.usian.pojo.ItemCat;
import com.usian.pojo.ItemDesc;
import com.usian.pojo.ItemParamItem;
import com.usian.service.ItemCatService;
import com.usian.service.ItemDescService;
import com.usian.service.ItemParamItemService;
import com.usian.service.ItemService;
import com.usian.util.RedisClient;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemResultVO;
import com.usian.vo.ItemVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/item")
public class ItemAction {


    @Autowired
    private ItemService itemService;


    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ItemCatService itemCatService;


    @Autowired
    private AmqpTemplate amqpTemplate; //操作MQ的模板对象


    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/selectItemInfo")
    Item selectItemById(@RequestParam("itemId") Long itemId){ //10000

        //1. 先查询缓存
        Item item = (Item) redisClient.hget("ITEM_DETAIL_ITEM", itemId + "");//null

        // 有返回
        if(item!=null){
            return item;
        }

        //没有，查询数据库，并存入到缓存
        if(redisClient.setNx("ITEM_DETAIL_ITEM_LOCK","",5, TimeUnit.SECONDS)){// 解决缓存的击穿
            try {
                // 设置这个锁的有效期
                item = itemService.getById(itemId);//null
                if(item == null){// 解决缓存的穿透
                    redisClient.hset("ITEM_DETAIL_ITEM", itemId + "",new Item());//null
                    redisClient.expire("ITEM_DETAIL_ITEM",30);//1天
                }else{
                    redisClient.hset("ITEM_DETAIL_ITEM", itemId + "",item);
                    redisClient.expire("ITEM_DETAIL_ITEM",24*60*60);//1天
                }
            } catch (Exception e) {
                // 释放锁
            }finally {
                redisClient.del("ITEM_DETAIL_ITEM_LOCK");
            }

        }else {
            selectItemById(itemId);//没拿到值在尝试一下
        }



        return item;

    }

    @RequestMapping("/preUpdateItem")
    public ItemResultVO preUpdateItem(@RequestParam("itemId") Long itemId){
        // 基本商品信息
        Item item = itemService.getById(itemId);

        // 商品的描述
        QueryWrapper<ItemDesc> condition1 = new QueryWrapper<>();
        condition1.eq("item_id",itemId);
        ItemDesc itemDesc = itemDescService.getOne(condition1);

        // 商品规格参数值
        QueryWrapper<ItemParamItem> condition = new QueryWrapper<>();
        condition.eq("item_id",itemId);
        ItemParamItem itemParamItem = itemParamItemService.getOne(condition);


        // 组合三个对象，变成前台需求的VO
        ItemResultVO itemResultVO = new ItemResultVO();
        itemResultVO.setItem(item);
        itemResultVO.setItemDesc(itemDesc.getItemDesc());
        itemResultVO.setItemParamItem(itemParamItem.getParamData());


        //根据cid 查询对应的类目名称  select * from tb_item_cat where id = ??
        ItemCat itemCate = itemCatService.getById(item.getCid());

        itemResultVO.setItemCat(itemCate.getName());//类目  ???


        return itemResultVO;

    }

    /**
     * 全量新增
     * 增量新增
     * @param itemVO
     */
    @RequestMapping("/insertTbItem")
    @Transactional
    public void insertTbItem(@RequestBody ItemVO itemVO){ // 华为mate40 ..... 1
        // part1: 将数据保存到mysql数据库
        // 基本商品信息，存放到 tb_item   ItemService
        // 生成一个商品的ID
        long itemID = IDUtils.genItemId();
        Date now = new Date();
        itemVO.setCreated(now);
        itemVO.setUpdated(now);
        itemVO.setId(itemID);
        itemService.save(itemVO);

        // 商品的描述   存放到 tb_item_desc  ItemDescService
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(itemID);
        itemDesc.setItemDesc(itemVO.getDesc());
        itemDesc.setCreated(now);
        itemDesc.setUpdated(now);
        itemDescService.save(itemDesc);

        // 商品规格参数值  存放到 tb_item_param_item  ItemParamItemService
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(itemID);
        itemParamItem.setParamData(itemVO.getItemParams());
        itemParamItem.setCreated(now);
        itemParamItem.setUpdated(now);
        itemParamItemService.save(itemParamItem);

        // 查询出当前新增商品的，分类名称
        ItemCat itemCat = itemCatService.getById(itemVO.getCid());
        itemVO.setCatName(itemCat.getName());

        //part2：将数据保存到 es数据库
        amqpTemplate.convertAndSend("usian_item","insert.item",itemVO);
    }

    @GetMapping("/selectTbItemAllByPage")
    public PageResult<Item> selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name="rows") Integer rows){
        //1. 调用mybatis plus 自带的API  进行查询
        QueryWrapper<Item> condition = new QueryWrapper<>();
        condition.orderByDesc("created");

        Page<Item> pageData = itemService.page(new Page<Item>(page, rows),condition);

        //2. 将Mybatis plus 页结果类型  转换为  项目中自定义的页结果类型
        PageResult<Item> result = new PageResult<>();
        result.setPageIndex(page);
        result.setTotalPage(pageData.getPages());
        result.setResult(pageData.getRecords());
        return result;

    }

    @GetMapping(value = "/")
    public List<Item> list() {
        return itemService.list();

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") String id) {
        return new ResponseEntity<>(itemService.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(@RequestBody Item params) {
        itemService.save(params);
        return new ResponseEntity<>("created successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        itemService.removeById(id);
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> delete(@RequestBody Item params) {
        itemService.updateById(params);
        return new ResponseEntity<>("updated successfully", HttpStatus.OK);
    }
}
