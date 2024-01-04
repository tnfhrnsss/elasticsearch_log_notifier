package com.lzdk.monitoring.es.utils;

import com.lzdk.monitoring.es.domain.LogSearchTraceKey;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class LogViewerResolver {
    public static String resolve(LogSearchTraceKey traceKey) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        String subPath = "";
        if (traceKey.traceByLogId()) {
            subPath = getSubPath(traceKey.getIndex(), traceKey.getLogId());
        } else {
            multiValueMap.add("q", StringUtils.join("traceId", ":", traceKey.getTraceId()));
            subPath = getSubPathByTraceId(traceKey.getIndex());
        }

        if (StringUtils.isEmpty(EsSearchProperties.getCustomViewerUrl())) {
            multiValueMap.add("pretty", "true");

        }
        String searchUrl = UriComponentsBuilder.fromUriString(EsSearchProperties.getBasicViewerUrl()).path(subPath).queryParams(multiValueMap).build().toString();
        return resolveByViewer(searchUrl);
    }

    private static String resolveByViewer(String searchUrl) {
        return StringUtils.join(EsSearchProperties.getCustomViewerUrl(), searchUrl);
    }

    @NotNull
    private static String getSubPath(String index, String id) {
        return StringUtils.join(index, "/", "_doc", "/", id);
    }

    @NotNull
    private static String getSubPathByTraceId(String index) {
        return StringUtils.join(index, "/", "_search");
    }
}
