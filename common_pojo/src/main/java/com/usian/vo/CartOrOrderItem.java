package com.usian.vo;

/**
 * @Title: CartItem
 * @Description: 购物车项  或者 订单项
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/23 14:32
 */
public class CartOrOrderItem {

    private Long id;
    private String title;
    private String image;
    private Integer num;// 购买的数量
    private Long price;
    private String sellPoint;

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
