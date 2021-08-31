package com.usian.util;

import com.usian.utils.JsonUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: EsUtil
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/17 15:26
 */
@Component
public class EsUtil {


    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * 判断索引是否存在
     * @param indexName
     */
    public boolean existIndex(String indexName)  {
          //创建索引操作客户端
        IndicesClient indices = restHighLevelClient.indices();
        GetIndexRequest getIndexRequest = new GetIndexRequest();
        getIndexRequest.indices("usian");
        boolean exists = false;
        try {
            exists = indices.exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * 创建一个索引
     * @param indexName  新增索引名
     * @param typeName   新增类型名
     * @param numberOfShards  索引分片数
     * @param numberOfReplicas 索引副本数
     * @param mappging  映射参数
     */
    public void createIndex(String indexName,String typeName,Integer numberOfShards,Integer numberOfReplicas,String mappging){
   // 1.获取操作索引的客户端
        IndicesClient indicesClient = restHighLevelClient.indices();

        //2. 创建一个索引
        Settings setting = Settings.builder().put("number_of_shards",numberOfReplicas).put("number_of_replicas",numberOfReplicas).build();

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName,setting);
        createIndexRequest.mapping(typeName,mappging, XContentType.JSON);
        try {
            indicesClient.create(createIndexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量新增文档到es库中
     * @param indexName  索引名
     * @param typeName  类型名
     * @param data    批量的数据
     */
    public void addDocument(String indexName, String typeName, List data){
        BulkRequest bulkRequest = new BulkRequest();

        for (Object e : data){
            //将java对象 转换成  json
            bulkRequest.add(new IndexRequest(indexName, typeName).source(JsonUtils.objectToJson(e),
                    XContentType.JSON));
        }

        try {
            BulkResponse bulkResponse =
                    restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *   mutimathc方式查询 且 高亮显示
     * @param indexName  索引名
     * @param typeName   类型名
     * @param conditionVal  查找值
     * @param T    返回值类型
     * @param integerFields  匹配的整型的字段
     * @param stringFields  匹配的字符串型 的字段
     * @param hightLightFields  高亮的字段
     * @return
     */
    public List multiMatchQuery(String indexName,String typeName,String conditionVal,Class T,String[] integerFields,String[] stringFields,String[] hightLightFields){
        //0. 封装查询请求对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //0.1  指定查询条件，且确定查找字段范围
        try {
            Integer.parseInt(conditionVal);
            // condittionField1  合并 到 conditionFeild2 里面
            ArrayList<String> list = new ArrayList<>();
           for (String s : stringFields){
               list.add(s);
           }
            for (String s : integerFields){
                list.add(s);
            }
            String[] newConditonFields = list.toArray(new String[list.size()]);//集合转数组
            // 查询字段的范围，是字符串+数值型类型的
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(conditionVal,newConditonFields));
        }catch (Exception e){
            // 查询字段的范围，是字符串类型的
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(conditionVal,stringFields));
        }
        //0.2 指定高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font style='color:red;font-size:23px'>");
        highlightBuilder.postTags("</font>");
        for (String hightLightField : hightLightFields){
            highlightBuilder.fields().add(new HighlightBuilder.Field(hightLightField));
        }
        searchSourceBuilder.highlighter(highlightBuilder);
        // 0.3  指定查询 索引，类型名的
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);

        //1. 执行搜索
        ArrayList result = new ArrayList(); // 封装查询结果结果的
        try {
            //1.1 触发es的搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 1.2. 处理搜索的结果
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                // 源文档内容
                String source = hit.getSourceAsString(); // 某条记录的json格式值
                // 将json-----》 java类型的对象
                Object data =  JsonUtils.jsonToPojo(source, T);// data--- 修改某个属性值？？？
                // 高亮值的 回设
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                String highLightValue = "";
                if (highlightFields != null) {
                    for (String hightLightField : hightLightFields){
                        HighlightField highlightField = highlightFields.get(hightLightField);
                        if(highlightField!=null){
                            Text[] fragments = highlightField.getFragments();
                            highLightValue =  fragments[0].toString(); // 高亮的值
                            Field field = T.getDeclaredField(hightLightField);
                            field.setAccessible(true);
                            field.set(data,highLightValue);  // esItemVo.ItemTitle = "测试";
                        }
                    }
                }
                result.add(data);
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //2. 返回查询的结果
        return result;
    }


}
