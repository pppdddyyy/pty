package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.usian.pojo.Item;
import com.usian.pojo.OrderItem;
import com.usian.vo.ESItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
public interface ItemMapper extends BaseMapper<Item> {

//

    //   public List<ESItemVO> queryAllFromMysql();
    //   自动封装LIMIT 语句
    public  <E extends IPage<ESItemVO>> E queryAllFromMysql(E page);

    //修改库存量的接口
    public int updateNum(@Param("id") String id, @Param("num") Integer num);



}
