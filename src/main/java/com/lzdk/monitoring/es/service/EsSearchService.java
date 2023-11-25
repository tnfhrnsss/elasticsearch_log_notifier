package com.lzdk.monitoring.es.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.lzdk.monitoring.es.domain.LogSearchResponseSdo;
import com.lzdk.monitoring.es.utils.EsSearchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EsSearchService {
    private final ElasticsearchClient elasticsearchClient;

    public List<LogSearchResponseSdo> find() {
        List<LogSearchResponseSdo> list = new ArrayList<>();

        try {
            Query fullNameQuery = MatchQuery.of(m ->  m.field("service").query(EsSearchProperties.getServices()))._toQuery();
            Query wildcard = WildcardQuery.of(m -> m.field("message").wildcard(EsSearchProperties.getMessage()))._toQuery();
            Query range = getTimestamp();
            Query excludeQuery = MatchQuery.of(m ->  m.field("message").query(EsSearchProperties.getExcludeMessages()))._toQuery();
            SearchResponse<Object> searchResponse = elasticsearchClient.search(s -> s
                .index(getIndexName())
                .query(q -> q.bool(b -> b.must(fullNameQuery, wildcard, range).mustNot(excludeQuery)))
                .sort(SortOptions.of(so -> so
                    .field(f -> f
                        .field("@timestamp")
                        .order(SortOrder.Asc)
                    )))
                .size(EsSearchProperties.getMaxCount()), Object.class);

            log.debug("total count. {}", searchResponse.hits().total());

            searchResponse.hits().hits().forEach(hit -> {
                LinkedHashMap<String, Object> hashMap = (LinkedHashMap) hit.source();
                list.add(new LogSearchResponseSdo(
                    hit.index(),
                    hit.id(),
                    (String) hashMap.get("service"),
                    (String) hashMap.get("message"),
                    (String) hashMap.get("stacktrace"),
                    (String) hashMap.get("@timestamp")));
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private String getIndexName() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return StringUtils.join(EsSearchProperties.getIndex(), formattedDate);
    }

    private Query getTimestamp() {
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        OffsetDateTime twoMinutesAgo = currentDateTime.minusMinutes(EsSearchProperties.getDuringMinutes());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String currentTime = currentDateTime.format(formatter);
        String towMinutesTime = twoMinutesAgo.format(formatter);
        return RangeQuery.of(m -> m.field("@timestamp")
            .from(towMinutesTime)
            .to(currentTime))
            ._toQuery();
    }
}
