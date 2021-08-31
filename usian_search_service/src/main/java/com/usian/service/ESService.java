package com.usian.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.usian.mapper.ItemMapper;
import com.usian.pojo.Item;
import com.usian.util.EsUtil;
import com.usian.vo.ESItemVO;
import com.usian.vo.ItemVO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: TotalItemsImportESUtil
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/17 14:17
 */
@Component
public class ESService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
//    private RestHighLevelClient esClient;
    private EsUtil esUtil;

    /**
     * 全量新增：
     *      初始化es中的数据
     *
     */
    public void importAll(){
        // 读取  mysql 数据库中全部的商品数据????
        /*
            SELECT a.`id`,a.`title` item_title,a.`sell_point` item_sell_point,a.`price` item_price,a.`image` item_image,c.`name` item_category_name, b.`item_desc`
            FROM tb_item a
            LEFT JOIN tb_item_desc b ON a.id = b.`item_id`
            LEFT JOIN tb_item_cat  c ON a.`cid` = c.`id`
            WHERE a.`status` = 1;

         */
        int page = 1;
        while (true){
            //读一页
            Page<ESItemVO> pageData = itemMapper.queryAllFromMysql(new Page<ESItemVO>(page, 10000));

            List<ESItemVO> records = pageData.getRecords();
            if(records==null || records.isEmpty()){//没有数据了就结束导入
                return ;
            }

//        esItemVOS.forEach(e-> System.out.println(e.getItem_title()));
           if (!esUtil.existIndex("usian")){
               esUtil.createIndex("usian","item",2,1,"{\n" +
                       "  \"_source\": {\n" +
                       "    \"excludes\": [\n" +
                       "      \"item_desc\"\n" +
                       "    ]\n" +
                       "  },\n" +
                       "  \"properties\": {\n" +
                       "    \"id\": {\n" +
                       "       \"type\": \"keyword\",\n" +
                       "       \"index\": false\n" +
                       "    },\n" +
                       "    \"item_title\": {\n" +
                       "      \"type\": \"text\",\n" +
                       "      \"analyzer\": \"ik_max_word\",\n" +
                       "      \"search_analyzer\": \"ik_smart\"\n" +
                       "    },\n" +
                       "    \"item_sell_point\": {\n" +
                       "      \"type\": \"text\",\n" +
                       "      \"analyzer\": \"ik_max_word\",\n" +
                       "      \"search_analyzer\": \"ik_smart\"\n" +
                       "    },\n" +
                       "    \"item_price\": {\n" +
                       "      \"type\": \"float\"\n" +
                       "    },\n" +
                       "    \"item_image\": {\n" +
                       "      \"type\": \"text\",\n" +
                       "      \"index\": false\n" +
                       "    },\n" +
                       "    \"item_category_name\": {\n" +
                       "      \"type\": \"text\",\n" +
                       "      \"analyzer\": \"ik_max_word\",\n" +
                       "      \"search_analyzer\": \"ik_smart\"\n" +
                       "    },\n" +
                       "    \"item_desc\": {\n" +
                       "      \"type\": \"text\",\n" +
                       "      \"analyzer\": \"ik_max_word\",\n" +
                       "      \"search_analyzer\": \"ik_smart\"\n" +
                       "    }\n" +
                       "  }\n" +
                       "}");
           }


            // 存入到ES 数据库中一页

            esUtil.addDocument("usian","item",records);



            page++;

        }
    }


    /**
     * 首页 搜索功能
     * @param q
     * @return
     */
    public List<ESItemVO> list(String q) {

        String[] integerFields = {"item_price"}; //整型 类型
        String[] stringFields = {"item_title","item_sell_point","item_category_name","item_desc"}; // 字符串的类型
        String[] hightFields ={"item_title","item_sell_point","item_price"} ;

        return  esUtil.multiMatchQuery("usian","item",q,ESItemVO.class,integerFields,stringFields,hightFields);

    }

    /**
     * 新增商品 同步到es
     * @param itemVO
     */
    public void insertItem(ItemVO itemVO) {
        // 将mq 附送的消息，格式化 成 es的结构
        ESItemVO esItemVO = new ESItemVO();
        esItemVO.setItem_sell_point(itemVO.getSellPoint());
        esItemVO.setId(itemVO.getId());
        esItemVO.setItem_desc(itemVO.getDesc());
        esItemVO.setItem_image(itemVO.getImage());
        esItemVO.setItem_price(itemVO.getPrice());
        esItemVO.setItem_title(itemVO.getTitle());
        esItemVO.setItem_category_name(itemVO.getCatName());


        ArrayList<ESItemVO> esItemVOS = new ArrayList<>();
        esItemVOS.add(esItemVO);

        esUtil.addDocument("usian","item",esItemVOS);

    }
}
