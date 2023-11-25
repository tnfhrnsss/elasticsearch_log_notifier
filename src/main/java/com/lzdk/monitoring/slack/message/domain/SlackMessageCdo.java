package com.lzdk.monitoring.slack.message.domain;

import com.lzdk.monitoring.utils.json.JsonSerializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlackMessageCdo implements JsonSerializable {
    private String title;

    private String message;

    private String viewPageUrl;

    @Override
    public String toString() {
        return toJson();
    }

    public static SlackMessageCdo create(String title, String message, String viewPageUrl) {
        return new SlackMessageCdo(title, StringUtils.substring(message, 0,500), viewPageUrl);
    }
}
