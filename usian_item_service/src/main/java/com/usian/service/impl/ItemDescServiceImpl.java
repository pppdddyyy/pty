package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.mapper.ItemDescMapper;
import com.usian.pojo.ItemDesc;
import com.usian.service.ItemDescService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品描述表 服务实现类
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
@Service
public class ItemDescServiceImpl extends ServiceImpl<ItemDescMapper, ItemDesc> implements ItemDescService {

}
