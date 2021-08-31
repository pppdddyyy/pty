package com.usian.api;

import com.usian.vo.CartOrOrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * @Title: CartFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 9:07
 */
@FeignClient("usian-cart-service")
public interface CartFeign {

    @RequestMapping("cart/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId);

    @RequestMapping("cart/showCart")
    public Collection<CartOrOrderItem> showCart(@RequestParam("userId") Long userId);

}
