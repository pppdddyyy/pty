package com.usian.controller;

import com.usian.service.ESService;
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
 * @create 2021/8/17 14:41
 */
@RestController
public class ESController {

    @Autowired
    private ESService esService;

    @RequestMapping("importAll")
    public void importAll(){
        esService.importAll();
    }


    @RequestMapping("/es/list")
    public List<ESItemVO> list(@RequestParam("q") String q){
        //查询es中的数据
       return  esService.list(q);


    }
}
