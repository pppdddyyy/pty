package com.usian.controller;

import com.usian.api.CartFeign;
import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import com.usian.vo.CartOrOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Title: CartController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/23 14:10
 */
@RestController
@RequestMapping("frontend/cart")
public class CartController {


    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private CartFeign cartFeign;



    @RequestMapping("deleteItemFromCart")
    public Result deleteItemFromCart(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
        try {
            if(userId == null){
                String cartDataJson = CookieUtils.getCookieValue(request, "CART_DATA", true);//[{xxx},{xxx}]
                Map<Long, CartOrOrderItem> cartData = JsonUtils.jsonToMap(cartDataJson,Long.class, CartOrOrderItem.class);
                cartData.remove(itemId);
                CookieUtils.setCookie(request,response,"CART_DATA",JsonUtils.objectToJson(cartData),true);

            }else {

            }
        } catch (Exception e) {
//            e.printStackTrace();
            return Result.error(e.getMessage());
        }

        return  Result.ok();
    }

    /**
     * 更改购物车中的某项的数量
     * B browser 浏览器/S Server 服务端(redis)
     *
     *      String ****   json 字符串
     *      张三   购物车1
     *
     *      李四   购物车
     *
     *      王五   购物车
     *      。。。。。
     *      hash  *****
     *      CART_DATA   张三    购物车1
     *                   李四    购物车2
     *
     *
     *
     * @param num
     * @param userId
     * @param itemId
     * @return
     */
    @RequestMapping("updateItemNum")
   public Result updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
        try {
            if(userId == null){
                String cartDataJson = CookieUtils.getCookieValue(request, "CART_DATA", true);//[{xxx},{xxx}]
                Map<Long, CartOrOrderItem> cartData = JsonUtils.jsonToMap(cartDataJson,Long.class, CartOrOrderItem.class); //旧的购物车数据
                // 从购物车中获取修改 数量的那个对应的购物车项
                CartOrOrderItem cartItem = cartData.get(itemId);
                //修改数量
                cartItem.setNum(num);// 引用传递

                CookieUtils.setCookie(request,response,"CART_DATA",JsonUtils.objectToJson(cartData),true);

            }else{//已登录

            }
        } catch (Exception e) {
            return  Result.error(e.getMessage());
        }

        return  Result.ok();

   }

    /**
     * 加入购物车
     *   测试案例：
     *          1. 购物车是空的  A
     *
     *          2. 购物车有数据，购物车项是否已经存在  A
     *              存在  ，将数量+1
     *              没有存在，增加一个购物车项
     *
     * @param userId
     * @param itemId
     * @return
     *
     */
    @RequestMapping("addItem")
    public Result addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response){
        if(userId == null){//没有登录过  cookie
            //1. 获取存放购物车数据的 容器 -----》cookie  "CART_DATA"  Map<ItemID,CartItem>
            String cartDataJson = CookieUtils.getCookieValue(request, "CART_DATA", true);//[{xxx},{xxx}]
            if(cartDataJson==null || cartDataJson.equals("")){//购物车是空的  第一次使用购物车
                Map<Long, CartOrOrderItem> cartData = new HashMap<>();
                // select * from tb_item where id = ??
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
                //2. 将购物车项信息加入到 cookie,将cookie回写到浏览器端
                CookieUtils.setCookie(request,response,"CART_DATA",JsonUtils.objectToJson(cartData),true);
            }else{
                Map<Long, CartOrOrderItem> cartData = JsonUtils.jsonToMap(cartDataJson,Long.class, CartOrOrderItem.class); //旧的购物车数据  "1"  A   1
                CartOrOrderItem cartItem = cartData.get(itemId);
                if(cartItem == null){ // 没有这个购物车项
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
                }else{//有这个购物车项
                    cartItem.setNum(cartItem.getNum()+1);
                }
                //2. 将购物车项信息加入到 cookie,将cookie回写到浏览器端
                CookieUtils.setCookie(request,response,"CART_DATA",JsonUtils.objectToJson(cartData),true);
            }

        }else{// 已经登录

            cartFeign.addItem(userId,itemId);



        }

        return Result.ok();
    }

    /**
     * 展示购物车的数据
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping("showCart")
    public Result showCart(@RequestParam("userId") Long userId,HttpServletRequest request){
        if(userId == null){//没有登录过
            String cartDataJson = CookieUtils.getCookieValue(request, "CART_DATA", true);
            Map<Long, CartOrOrderItem> cartData = JsonUtils.jsonToMap(cartDataJson,Long.class, CartOrOrderItem.class);
            if(cartData!=null){
                Collection<CartOrOrderItem> cartItems = cartData.values();
                return  Result.ok(cartItems);
            }
            return Result.ok();
        }else{

            Collection<CartOrOrderItem> cartItems =   cartFeign.showCart(userId);
          return Result.ok(cartItems);

        }

    }
}
