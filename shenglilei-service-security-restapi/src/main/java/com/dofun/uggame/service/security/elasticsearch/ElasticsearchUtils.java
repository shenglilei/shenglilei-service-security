package com.dofun.uggame.service.security.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticsearchUtils {
    private static  String SHARDS = "index.number_of_shards";
    private static  String REPLICAS = "index.number_of_replicas";

	private static Logger logger= LoggerFactory.getLogger(ElasticsearchUtils.class);

	private static RestHighLevelClient highLevelRestClient = ElasticSearchSingletonClient.getRestHighLevelClientInstance();

    /**
     *
     * 方法描述
     * 批量添加index
     * @param type
     * @throws Exception
     * @变更记录 2018年11月21日 上午11:33:18  qiulisan 创建
     */
    public static void bulkIndex(JSONArray data, String type) throws Exception {
        long l = 0L;
        long l2 =0L;

        BulkRequest bulkRequest = new BulkRequest();
        for(int i=0;i<data.size();i++){
            createIndex("companydb"+data.getJSONObject(i).getString("companyId"),3,1);
            bulkRequest.add(new IndexRequest("companydb"+data.getJSONObject(i).getString("companyId"), type, data.getJSONObject(i).getString("mobile")+data.getJSONObject(i).getString("companyId"))
                    .source(data.getJSONObject(i).toJSONString(), XContentType.JSON));
        }
        try {
            l = System.currentTimeMillis();
            BulkResponse bulkResponse = highLevelRestClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            l2 = System.currentTimeMillis();
            if(bulkResponse.hasFailures()){
                logger.info("批量添加es索引失败："+bulkResponse.buildFailureMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        logger.info("批量添加es索引完成,用时{}",  (l2-l));

    }
    public static boolean createIndex(String indexName, int shards, int replicas) throws Exception {
        if (isExistsIndex(indexName)) {
            logger.error(indexName + " 索引已经存在");
            return false;
        }
         //日志报错
        //Settings.Builder builder = Settings.builder().put(SHARDS, shards).put(REPLICAS, replicas);
        Settings.Builder builder = null;

        CreateIndexRequest request = new CreateIndexRequest(indexName).settings(builder);
        CreateIndexResponse response = highLevelRestClient.indices().create(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            logger.info("索引{}创建成功",indexName);
            return true;
        }
        return false;
    }
    public static boolean isExistsIndex(String indexName) throws Exception {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        return highLevelRestClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    //批量操作 有则更新，无则插入
    public static void insertBatch(String index, String type,JSONArray  jsonArray) {
        // 创建批量操作请求
        BulkRequest bulkRequest = new BulkRequest();
        Map<String,Object> map=null;
        try {
            for (int i=0;i<jsonArray.size();i++) {
                map=new HashMap<>();
                String rowKey = new String(jsonArray.getJSONObject(i).getBytes("rowKey"));
                String family = new String(jsonArray.getJSONObject(i).getBytes("family"));
                String qualifier = new String(jsonArray.getJSONObject(i).getBytes("qualifier"));
                if("eid".equals(family)){
                    long value = ElasticsearchUtils.bytes2long(jsonArray.getJSONObject(i).getBytes("value"));
                    System.out.println("eid值==="+value);
                    map.put(qualifier,value);
                }else{
                    String value = new String(jsonArray.getJSONObject(i).getBytes("value"));
                    System.out.println("value==="+value);
                    map.put(qualifier,value);
                }
                IndexRequest indexRequest = new IndexRequest(index)
                        .type(type)
                        .id(rowKey)
                        .source(map);

                // 创建更新请求，指定index，type,id,如果indexRequest 有值 （存在该数据）则用doc指定的内容更新indexRequest中指定的source，如果不存在该数据，则插入一条indexRequest指定的source数据
                UpdateRequest updateRequest = new UpdateRequest(index, type, rowKey)
                        .doc(map)
                        .upsert(indexRequest);
                // 将更新请求加入批量操作请求
                bulkRequest.add(updateRequest);
            }
            // 执行批量操作
            highLevelRestClient.bulk(bulkRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //有则更新，无则插入
    public static void upsert(String index,String type,String id ,Map<String,Object> map) {
        try {

            XContentBuilder xContentBuilder = jsonBuilder().startObject();
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for(Map.Entry<String, Object> entry:entries){
                xContentBuilder.field(entry.getKey(),entry.getValue());
            }

            xContentBuilder.endObject();
            IndexRequest indexRequest = new IndexRequest(index, type, id)
                    .source(xContentBuilder);
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .doc(xContentBuilder
                    ).upsert(indexRequest).retryOnConflict(10);
            UpdateResponse updateResponse = highLevelRestClient.update(updateRequest, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    logger.info("更新index失败:"+failure.reason());
                }
            }else{
                logger.info("更新index成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static long bytes2long(byte[] bs)  {
        if(bs==null){
            logger.info("bs 为null");
            return 0;
        }
        int bytes = bs.length;
        if(bytes > 1) {
            if((bytes % 2) != 0 || bytes > 8) {
                //throw new Exception("not support");
                logger.info("not support");
                return 0;
            }}
        switch(bytes) {
            case 0:
                return 0;
            case 1:
                return (long)((bs[0] & 0xff));
            case 2:
                return (long)((bs[0] & 0xff) <<8 | (bs[1] & 0xff));
            case 4:
                return (long)((bs[0] & 0xffL) <<24 | (bs[1] & 0xffL) << 16 | (bs[2] & 0xffL) <<8 | (bs[3] & 0xffL));
            case 8:
                return (long)((bs[0] & 0xffL) <<56 | (bs[1] & 0xffL) << 48 | (bs[2] & 0xffL) <<40 | (bs[3] & 0xffL)<<32 |
                        (bs[4] & 0xffL) <<24 | (bs[5] & 0xffL) << 16 | (bs[6] & 0xffL) <<8 | (bs[7] & 0xffL));
            default:
                //throw new Exception("not support");
                logger.info("not support");
                return 0;
        }
    }
	/**
     *
         * 方法描述
         * 条件拼接查询
         * @param type
         * @return
         * @throws UnknownHostException
         * @变更记录 2016年11月11日 上午10:30:00  tanguohong 创建
     */
    public static Integer search(String companyId, String type) throws Exception {
        SearchRequest searchRequest = new SearchRequest(companyId);
        Map<String, Object> query =new HashMap<>();
        query.put("222","222");

        queryBuilder(null,null,query,searchRequest);

        SearchResponse searchResponse = highLevelRestClient.search(searchRequest, RequestOptions.DEFAULT);
        return 0;
    }
    private static void queryBuilder(Integer pageIndex, Integer pageSize, Map<String, Object> query, SearchRequest searchRequest) {
        if (query != null && !query.keySet().isEmpty()) {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 按评分排序
            //searchSourceBuilder.sort("_score");
            if (pageIndex != null && pageSize != null) {
                searchSourceBuilder.size(pageSize);
                if (pageIndex <= 0) {
                    pageIndex = 0;
                }
                searchSourceBuilder.from((pageIndex - 1) * pageSize);
            }
            BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
          /*  query.keySet().forEach(key -> {
                boolBuilder.should(QueryBuilders.matchQuery(key, query.get(key)));
            });*/

            boolBuilder.must(QueryBuilders.termQuery("orderId", 22974L));
            searchSourceBuilder.query(boolBuilder);
            searchRequest.source(searchSourceBuilder);
        }
    }


    public static void main(String[] args) throws Exception {
        search("fb_report","");
    }
}
