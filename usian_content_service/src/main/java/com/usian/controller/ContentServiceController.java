package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.usian.cons.ContConst;
import com.usian.pojo.Content;
import com.usian.pojo.ContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.service.ContentService;
import com.usian.util.RedisClient;
import com.usian.utils.Result;
import com.usian.vo.ADVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: ContentService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/11 15:08
 */
@RestController
@RequestMapping("/content")
public class ContentServiceController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private RedisClient redisClient;
    /**
     * 查询大广告的数据
     * @return
     */
    @RequestMapping("/selectFrontendContentByAD")
    public List<ADVO> selectFrontendContentByAD(){
        // 先查询缓存
        List<ADVO>  redisData = (List<ADVO>) redisClient.get("USIAN_INDEX_BIG_AD");
        //有返回
        if(redisData!=null) return redisData;

        //没有，先查，在设置到redis
        //SELECT * FROM tb_content WHERE category_id = 89
        QueryWrapper<Content> condition = new QueryWrapper<>();
        condition.eq("category_id",ContConst.BIG_AD_CATEGORY_ID);

        List<Content> ads = contentService.list(condition);

        List<ADVO> advos = new ArrayList<>();//新类型集合
        for (Content content : ads){
            ADVO advo = new ADVO();
            advo.setAlt(content.getTitle());
            advo.setWidth(ContConst.BIG_AD_WIDTH);
            advo.setWidthB(550);
            advo.setHeight(240);
            advo.setHeightB(240);
            advo.setSrc(content.getPic());
            advo.setSrcB(content.getPic2());
            advo.setHref(content.getUrl());
            advos.add(advo);
        }

        redisClient.set("USIAN_INDEX_BIG_AD",advos);
        return advos;
    }


    @RequestMapping("/deleteContentByIds")
    public void deleteContentByIds(@RequestParam("ids") Long ids){
        contentService.removeById(ids);
    }

    @RequestMapping("/insertTbContent")
    public void insertTbContent(@RequestBody Content content){
        content.setCreated(new Date());
        contentService.save(content);

    }
    @RequestMapping("/selectTbContentAllByCategoryId")
    public List<Content> selectTbContentAllByCategoryId (@RequestParam("categoryId") Long categoryId){
        //select * from content where category_id = ?


        QueryWrapper<Content> condition = new QueryWrapper<>();
        condition.eq("category_id",categoryId);

       return   contentService.list(condition);
    }

    @RequestMapping("/updateContentCategory")
    public void updateContentCategory(@RequestParam("id") Long id,@RequestParam("name") String name){
        ContentCategory contentCategory = contentCategoryService.getById(id);
        contentCategory.setName(name);
        contentCategory.setUpdated(new Date());

        contentCategoryService.updateById(contentCategory);
    }


    @RequestMapping("/deleteContentCategoryById")
    public boolean deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        ContentCategory contentCategory = contentCategoryService.getById(categoryId);

        //1. 如果当前节点是父节点，那就不能进行删除操作
        if(contentCategory.getIsParent()){
            return false;
        }



//            contentCategoryService.removeById(categoryId)//真删除

        // update content_category  set status = 2 and update =now() where id = ?

        // 创建一个修改之后的contentCategory
        //1. 获取数据库旧的那条记录
        //方式一

        contentCategory.setStatus(2);
        contentCategory.setUpdated(new Date());

        contentCategoryService.updateById(contentCategory);

        // 考虑是否影响你的父节点
        // 如果当前节点是父节点的最后一个孩子

        // select * from contet_catetory where parent_id = 当前子节点的父节点 and status = 1

        QueryWrapper<ContentCategory> condition = new QueryWrapper<>();
        condition.eq("parent_id",contentCategory.getParentId()).eq("status","1");

        List<ContentCategory> xiongdiNodes = contentCategoryService.list(condition);

        if(xiongdiNodes.size()==0){//没用兄弟了

            // update content_category set is_parent= 0 , updated= now() where id = ??
            UpdateWrapper<ContentCategory> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",contentCategory.getParentId()).setSql("is_parent= 0 , updated = now()");

            contentCategoryService.update(updateWrapper);

        }


        // 考虑是否影响你的子节点
        // 方案一： 父删掉子删除
        //方案二：有孩子，父就不能删掉


        return true;



    }
    @RequestMapping("/insertContentCategory")
    @Transactional
    public void insertContentCategory(@RequestParam("parentId") Long parentId, @RequestParam("name") String name){

      // 新增一个叶子节点
        ContentCategory contentCategory = new ContentCategory();

        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());

        contentCategoryService.save(contentCategory);


        // 影响父节点
        // update  contentCategory set is_parent= 1 wehere id = 叶子节点的父ID and is_parent= 0;

//        ContentCategory parentNode = contentCategoryService.getById(parentId);
//        contentCategory.setIsParent(true);
//        contentCategory.setUpdated(new Date());

        UpdateWrapper<ContentCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",parentId).eq("is_parent","0").setSql("is_parent= 1 , updated = now()");

        contentCategoryService.update(updateWrapper);

    }

    @RequestMapping("selectContentCategoryByParentId")
    public List<ContentCategory> selectContentCategoryByParentId(@RequestParam(name = "id") Long id){

        //Select * from tb_content_category where parent_id = ?? and status  = 1

        QueryWrapper<ContentCategory> condition = new QueryWrapper<>();
        condition.eq("parent_id",id).eq("status","1");

        return contentCategoryService.list(condition);



    }
}
