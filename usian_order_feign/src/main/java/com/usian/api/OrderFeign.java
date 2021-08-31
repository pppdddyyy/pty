package com.usian.api;

import com.usian.dto.OrderDTO;
import com.usian.vo.CartOrOrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: OrderFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 15:14
 */
@FeignClient("usian-order-service")
public interface OrderFeign {

    @RequestMapping("order/goSettlement")
    public List<CartOrOrderItem> goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId);

    @RequestMapping("order/insertOrder")
    String insertOrder(@RequestBody OrderDTO orderDTO);
}
