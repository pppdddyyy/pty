package com.usian.controller;

import com.usian.api.ESFeign;
import com.usian.vo.ESItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: ESController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/18 8:34
 */
@RestController
@RequestMapping("/frontend/searchItem")
public class IndexESController {

    @Autowired
    private ESFeign esFeign;

    @RequestMapping("list")
    public List<ESItemVO> list(@RequestParam("q") String q){

        List<ESItemVO> data =  esFeign.list(q);

        return data;

    }
}
