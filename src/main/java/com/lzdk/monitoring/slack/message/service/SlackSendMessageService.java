package com.lzdk.monitoring.slack.message.service;

import com.lzdk.monitoring.es.utils.EsSearchProperties;
import com.lzdk.monitoring.slack.message.domain.SlackMessageCdo;
import com.lzdk.monitoring.slack.utils.SlackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackSendMessageService {
    private final SlackMessageService slackMessageService;

    private final SlackBlockService slackBlockService;

    public void message(SlackMessageCdo slackMessageCdo) {
        try {
            slackMessageService.publish(
                SlackProperties.getChannelId(),
                slackBlockService.makeChannelBlocks(slackMessageCdo.getTitle(), slackMessageCdo.getMessage(), slackMessageCdo.getViewPageUrl())
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
