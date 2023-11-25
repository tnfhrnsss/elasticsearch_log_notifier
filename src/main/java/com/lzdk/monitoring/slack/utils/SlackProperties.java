package com.lzdk.monitoring.slack.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlackProperties {
    private static String token = "";

    private static String channelId = "";

    @Value("${monitoring.slack.token:}")
    public void setToken(String value) {
        token = value;
    }

    @Value("${monitoring.slack.channel.id:}")
    public void setChannelId(String value) {
        channelId = value;
    }

    public static String getToken() {
        return token;
    }

    public static String getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "SlackConfig{"
            + "token="
            + token
            + '}';
    }
}
