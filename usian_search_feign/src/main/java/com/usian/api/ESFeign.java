package com.usian.api;

import com.usian.vo.ESItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: ESFegin
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/18 8:38
 */
@FeignClient("usian-search-service")
public interface ESFeign {

    @RequestMapping("/es/list")
    public List<ESItemVO> list(@RequestParam("q") String q);

}
