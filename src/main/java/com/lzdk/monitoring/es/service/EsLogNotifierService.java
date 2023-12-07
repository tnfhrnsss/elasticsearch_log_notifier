package com.lzdk.monitoring.es.service;

import java.util.List;

import com.lzdk.monitoring.es.domain.LogSearchResponseSdo;
import com.lzdk.monitoring.es.utils.LogViewerResolver;
import com.lzdk.monitoring.slack.message.domain.SlackMessageCdo;
import com.lzdk.monitoring.slack.message.service.SlackSendMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
                    LogViewerResolver.resolve(logSearchResponseSdo.getTraceKey())
                )
            );
        }
    }

    private void sendMessage(SlackMessageCdo slackMessageCdo) {
        slackSendMessageService.message(slackMessageCdo);
    }
}
