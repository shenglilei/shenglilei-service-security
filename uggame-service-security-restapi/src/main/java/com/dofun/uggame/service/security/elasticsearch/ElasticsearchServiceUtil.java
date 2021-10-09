package com.dofun.uggame.service.security.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 索引操作
 * shenglilei
 */
@Service
public class ElasticsearchServiceUtil {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    public void createIndex(Map<String, Object> params,
                            String indexName,
                            String id) throws IOException {
        logger.info(" ===== > 创建索引参数: params={}, indexName={}", params, indexName);

        IndexRequest request = new IndexRequest(indexName);
        request.opType(DocWriteRequest.OpType.CREATE);
        request.id(id);
        request.source(params);

        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        logger.info(" ===== > 创建索引结果: " + JSON.toJSONString(indexResponse));
        if (indexResponse != null) {
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.debug("新增文档成功: index={}, id={}, " + indexResponse.getIndex() + indexResponse.getId());
            }
            // 分片处理信息
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.debug(" ===== > 分片处理信息..... Handle the situation where number of successful shards is less than total shards");
            }
            // 如果有分片副本失败，可以获得失败原因信息
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    logger.debug(" ===== > 副本失败原因：{}", reason);
                }
            }
        }
    }

    /**
     * 判断索引是否存在
     */
    public Boolean existsIndex(String indexName) throws IOException {
        logger.info(" ===== > 判断索引是否存在: indexName={}", indexName);
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        logger.info(" ===== > existsIndex: {}", exists ? "存在" : "不存在");
        return exists;
    }

    /**
     * 删除索引index 相当于 删除es的一个数据库
     */
    public Boolean deleteIndex(String indexName) throws IOException {
        logger.info(" ===== > 删除索引参数: indexName={}", indexName);
        Boolean flag = existsIndex(indexName);
        if (!flag) {
            return false;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        logger.info(" ===== > deleteIndex: {}", JSON.toJSONString(delete));
        return true;
    }

    /**
     * 根据docId获取索引下的文档信息
     */
    public String getIndex(String indexName, String type,
                           String id) throws IOException {
        logger.info(" ===== > 获取文档信息参数: indexName={}, id={}", indexName, id);
        GetRequest getRequest = new GetRequest(indexName, type, id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > getIndex: " + JSON.toJSONString(getResponse));
        return JSON.toJSONString(getResponse);
    }

    /**
     * 插入文档数据
     */
    public Integer documentInsert(Map<String, Object> params,
                                  String indexName) throws IOException {
        logger.info(" ===== > 插入文档数据: params={}, indexName={}", params, indexName);
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.source(JSON.toJSONString(params), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        logger.info(" ===== > documentInsert: {}" , JSON.toJSONString(response));
        return 1;
    }

    /**
     * 删除文档数据
     */
    public Integer documentDelete(String indexName, String type,
                                  String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, type, id);
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > documentDelete: " + JSON.toJSONString(response));
        return 1;
    }

    /**
     * 更新文档信息
     */
    public Integer documentUpdate(Map<String, Object> params,
                                  String indexName, String type,
                                  String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, type, id);
        updateRequest.doc(params);
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > documentUpdate: " + JSON.toJSONString(update));
        return 1;
    }

    /**
     * 批量往索引中插入数据
     */
    public Integer documentBatchInsert(List<Map<String, Object>> data,
                                       String indexName) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        data.forEach(datum -> {
            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.source(JSON.toJSONString(datum), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > documentBatchInsert: " + JSON.toJSONString(bulkResponse));
        return data.size();
    }

    /**
     * 批量更新索引中的数据
     */
    public Integer documentBatchUpdate(Map<String, Map<String, Object>> data,
                                       String indexName, String type) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        data.forEach((id, params) -> {
            UpdateRequest updateRequest = new UpdateRequest(indexName, type, id);
            updateRequest.doc(params);
            bulkRequest.add(updateRequest);
        });
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > documentBatchUpdate: " + JSON.toJSONString(bulkResponse));
        return data.size();
    }

    /**
     * 批量删除数据
     */
    public Integer documentBatchDelete(List<String> ids,
                                       String indexName) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        ids.forEach(id -> {
            DeleteRequest indexRequest = new DeleteRequest(indexName);
            indexRequest.id(id);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(" ===== > documentBatchDelete: " + JSON.toJSONString(bulkResponse));
        return ids.size();
    }

    /**
     * 综合查询
     */
    public List<Map<String, Object>> documentQuery(Map<String, Object> query,
                                                   String indexName) throws IOException {
        List<Map<String, Object>> result = Lists.newArrayList();
        // 可以不指定索引，也可以指定多个
        SearchRequest searchRequest = new SearchRequest(indexName);
        queryBuilder(null, null, query, searchRequest);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            map.put("id", hit.getId());
            result.add(map);
        }
        return result;
    }


    /**
     * 一个 bool 过滤器由三部分组成：
     * {
     * "bool" : {
     * "must" :     [],
     * "should" :   [],
     * "must_not" : [],
     * }
     * }
     * must 所有的语句都 必须（must） 匹配，与 AND 等价。
     * must_not 所有的语句都 不能（must not） 匹配，与 NOT 等价。
     * should 至少有一个语句要匹配，与 OR 等价。
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param searchRequest
     */
    private void queryBuilder(Integer pageIndex, Integer pageSize, Map<String, Object> query, SearchRequest searchRequest) {
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
            query.keySet().forEach(key -> {
                boolBuilder.should(QueryBuilders.matchQuery(key, query.get(key)));
            });
            searchSourceBuilder.query(boolBuilder);
            searchRequest.source(searchSourceBuilder);
        }
    }

}
