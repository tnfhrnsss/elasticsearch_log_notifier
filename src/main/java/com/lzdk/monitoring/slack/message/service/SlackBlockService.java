package com.lzdk.monitoring.slack.message.service;

import java.util.stream.Collectors;

import com.lzdk.monitoring.slack.message.domain.BlockList;
import com.lzdk.monitoring.slack.message.domain.DmBlock;
import com.lzdk.monitoring.slack.message.domain.HeaderBlock;
import com.lzdk.monitoring.slack.message.domain.MarkdownBlock;
import com.lzdk.monitoring.slack.utils.SlackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackBlockService {
    public String makeChannelBlocks(String title, String message, String viewPageUrl) {
        BlockList blockList = BlockList.addHeader(HeaderBlock.create(title));
        blockList.addDmBlock(DmBlock.create("For more information, click the button.", viewPageUrl));
        String blockMessage = StringUtils.join("```", message, "```");
        blockList.addMentionBlock(MarkdownBlock.create(blockMessage));
        return blockList.toJson();
    }
}
