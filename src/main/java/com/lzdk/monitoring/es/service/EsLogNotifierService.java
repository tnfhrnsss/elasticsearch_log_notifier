package com.lzdk.monitoring.es.service;

import java.util.List;

import com.lzdk.monitoring.es.domain.LogSearchResponseSdo;
import com.lzdk.monitoring.es.domain.LogSearchTraceKey;
import com.lzdk.monitoring.es.utils.EsSearchProperties;
import com.lzdk.monitoring.slack.message.domain.SlackMessageCdo;
import com.lzdk.monitoring.slack.message.service.SlackSendMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class EsLogNotifierService {
    private final EsSearchService esSearchService;

    private final SlackSendMessageService slackSendMessageService;

    @Async("taskExecutor")
    @Scheduled(cron = "${jobs.cronSchedule:}")
    public void esLogNotify() {
        List<LogSearchResponseSdo> list = esSearchService.find();
        for (LogSearchResponseSdo logSearchResponseSdo : list) {
            sendMessage(
                SlackMessageCdo.create(
                    StringUtils.join(logSearchResponseSdo.getTimeStamp(), " ", logSearchResponseSdo.getService()),
                    logSearchResponseSdo.getMessage(),
                    makeViewPageUrl(logSearchResponseSdo.getTraceKey())
                )
            );
        }
    }

    private void sendMessage(SlackMessageCdo slackMessageCdo) {
        slackSendMessageService.message(slackMessageCdo);
    }

    private String makeViewPageUrl(LogSearchTraceKey traceKey) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("pretty", "true");
        String subPath = "";
        if (traceKey.traceByLogId()) {
            subPath = getSubPath(traceKey.getIndex(), traceKey.getLogId());
        } else {
            multiValueMap.add("q", StringUtils.join("traceId", ":", traceKey.getTraceId()));
            subPath = getSubPathByTraceId(traceKey.getIndex());
        }
        return UriComponentsBuilder.fromUriString(EsSearchProperties.getViewPageUrl()).path(subPath).queryParams(multiValueMap).build().toString();
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
