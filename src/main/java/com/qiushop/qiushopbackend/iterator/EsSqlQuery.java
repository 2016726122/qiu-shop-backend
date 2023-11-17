package com.qiushop.qiushopbackend.iterator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class EsSqlQuery implements EsSqlQueryInterface<EsQueryIterator>{
    //插叙的 SQL 语句
    private String query;
    //fetchsize 参数
    private Long fetchSize;
    //分页查询的 cursor 参数
    private String cursor;
    //分页查询时，只传入 cursor 参数即可
    public EsSqlQuery(String cursor) {
        this.cursor = cursor;
    }
    //第一次查询时，传入 query 和 fetchsize 参数
    public EsSqlQuery(String query, Long fetchSize) {
        this.query = query;
        this.fetchSize = fetchSize;
    }
    //创建具体的迭代器 EsQueryIterator
    public EsQueryIterator iterator() {
        return new EsQueryIterator(this.query, this.fetchSize);
    }
}
