package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.mapper.ItemParamItemMapper;
import com.usian.pojo.ItemParamItem;
import com.usian.service.ItemParamItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品规格和商品的关系表 服务实现类
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
@Service
public class ItemParamItemServiceImpl extends ServiceImpl<ItemParamItemMapper, ItemParamItem> implements ItemParamItemService {

}
