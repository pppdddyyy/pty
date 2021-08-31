package com.usian.api;

import com.usian.pojo.Content;
import com.usian.pojo.ContentCategory;
import com.usian.utils.Result;
import com.usian.vo.ADVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: ContentFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/11 15:06
 */
@FeignClient("usian-content-service")
public interface ContentFeign {


    @RequestMapping("/content/selectFrontendContentByAD")
    public List<ADVO> selectFrontendContentByAD();

    @RequestMapping("/content/deleteContentByIds")
    public void deleteContentByIds(@RequestParam("ids") Long ids);
    @RequestMapping("/content/insertTbContent")
    public void insertTbContent(@RequestBody Content content);
    @RequestMapping("/content/selectTbContentAllByCategoryId")
    public List<Content> selectTbContentAllByCategoryId (@RequestParam("categoryId") Long categoryId);

    @RequestMapping("/content/updateContentCategory")
    public void updateContentCategory(@RequestParam("id") Long id,@RequestParam("name") String name);
    @RequestMapping("/content/selectContentCategoryByParentId")
    public List<ContentCategory> selectContentCategoryByParentId(@RequestParam(name = "id") Long id);

    @RequestMapping("/content/insertContentCategory")
    public void insertContentCategory(@RequestParam("parentId") Long parentId,@RequestParam("name") String name);

    @RequestMapping("/content/deleteContentCategoryById")
    public boolean deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);
}
