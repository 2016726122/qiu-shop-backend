package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.iterator.EsQueryIterator;
import com.qiushop.qiushopbackend.iterator.EsSqlQuery;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class EsQueryService {

    public Object queryEsBySql(EsSqlQuery esSqlQuery) {
        EsQueryIterator iterator = esSqlQuery.iterator();
        Stream<Map<String, Object>> resultStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
        return  resultStream.collect(Collectors.toList());
    }
}
