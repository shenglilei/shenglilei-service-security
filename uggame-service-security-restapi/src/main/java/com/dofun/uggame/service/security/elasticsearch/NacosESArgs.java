package com.dofun.uggame.service.security.elasticsearch;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component(value = "nacosESArgs")
@RefreshScope
@Getter
public class NacosESArgs {

    @Value(value ="${elasticSearch.host:10.0.65.69}")
    private String EsHost;

    @Value(value ="${elasticSearch.port:8081}")
    private int EsPort;

    @Value(value ="${elasticSearch.client.connectNum:100}")
    private Integer EsConnectNum;

    @Value(value = "${elasticSearch.client.connectPerRoute:100}")
    private  Integer EsConnectPerRoute;
}
