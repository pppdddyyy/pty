package com.usian.controller;

import com.usian.service.CartService;
import com.usian.vo.CartOrOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @Title: CartController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 9:08
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId){
        cartService.addItem(userId,itemId);

    }

    @RequestMapping("/showCart")
    public Collection<CartOrOrderItem> showCart(@RequestParam("userId") Long userId){
        return cartService.showCart(userId);
    }
}
