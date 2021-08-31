package com.usian.service;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.util.RedisClient;
import com.usian.vo.CartOrOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: CartService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 9:10
 */
@Service
public class CartService {

    @Autowired
    private RedisClient redisClient;


    @Autowired
    private ItemFeign itemFeign;

    /**
     *   一个购物车项的信息，itemId--- 标题，卖点，图片 ---》 tb_item
     *
     * 新增购物车项到购物车  hash
     * @param userId
     * @param itemId
     */
    public void addItem(Long userId, Long itemId) {
        //1. 从redis中获取 购物车数据
        Map<Long, CartOrOrderItem> cartData = (Map<Long, CartOrOrderItem>) redisClient.hget("CART_DATA", userId + "");
        //2. 判断当前这个人，购物车中是否有数据
        if (cartData==null || cartData.isEmpty()){
            cartData = new HashMap<>();
            //3. 没有数据代表第一次使用购物车，直接做新增操作
            Item item = itemFeign.selectItemById(itemId);
            // 根据用户购买商品的ID，组装成一个购物车项
            CartOrOrderItem cartItem = new CartOrOrderItem();
            cartItem.setId(itemId);
            cartItem.setImage(item.getImage());
            cartItem.setNum(1);
            cartItem.setPrice(item.getPrice());
            cartItem.setSellPoint(item.getSellPoint());
            cartItem.setTitle(item.getTitle());
            cartData.put(itemId,cartItem);
//            //将最新的购物车数据，写入到 redis中
//            redisClient.hset("CART_DATA", userId + "",cartData);
        }else {
            //4. 如果有数据，还有在判断，该购物车项有没有，有 改数量+1  没有就新增一个
            CartOrOrderItem cartItem = cartData.get(itemId+"");
            if(cartItem == null){
                Item item = itemFeign.selectItemById(itemId);
                // 根据用户购买商品的ID，组装成一个购物车项
                cartItem = new CartOrOrderItem();
                cartItem.setId(itemId);
                cartItem.setImage(item.getImage());
                cartItem.setNum(1);
                cartItem.setPrice(item.getPrice());
                cartItem.setSellPoint(item.getSellPoint());
                cartItem.setTitle(item.getTitle());
                cartData.put(itemId,cartItem);

            }else {
                cartItem.setNum(cartItem.getNum()+1);
            }
        }

        redisClient.hset("CART_DATA", userId + "",cartData);



    }

    /**
     * 从redis 中获取 指定人的购物车项数据
     * @param userId
     * @return
     */
    public Collection<CartOrOrderItem> showCart(Long userId) {

        //1. 从redis中获取 购物车数据
        Map<Long, CartOrOrderItem> cartData = (Map<Long, CartOrOrderItem>) redisClient.hget("CART_DATA", userId + "");

        return cartData.values();
    }
}
