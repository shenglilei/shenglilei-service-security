package com.dofun.uggame.service.security.elasticsearch;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * @Description ESConfig ES配置类
 * @Author shenglilei
 */
@Configuration
@ComponentScan(basePackageClasses= ElasticsearchClientFactory.class)
public class ElasticsearchConfig {

    @Resource
    private NacosESArgs nacosESArgs;

    @Bean
    public HttpHost httpHost(){
        return new HttpHost(nacosESArgs.getEsHost(), nacosESArgs.getEsPort(),"http");
    }

    @Bean(initMethod="init", destroyMethod="close")
    public ElasticsearchClientFactory getFactory(){
        return ElasticsearchClientFactory.
                build(httpHost(), nacosESArgs.getEsConnectNum(), nacosESArgs.getEsConnectPerRoute());
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient(){
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient(){
        return getFactory().getRhlClient();
    }

}