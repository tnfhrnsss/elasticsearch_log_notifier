package com.lzdk.monitoring.es.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EsSearchProperties {
    private static String index = "";

    private static String services = "";

    private static int maxCount = 0;

    private static int duringMinutes = 0;

    private static String message = "";

    private static String excludeMessages = "";

    private static String viewPageUrl = "";

    @Value("${monitoring.es.search.index:}")
    public void setIndex(String value) {
        index = value;
    }

    public static String getIndex() {
        return index;
    }

    public static int getMaxCount() {
        return maxCount;
    }

    @Value("${monitoring.es.search.max-count:10}")
    public void setMaxCount(int value) {
        maxCount = value;
    }

    public static int getDuringMinutes() {
        return duringMinutes;
    }

    @Value("${monitoring.es.search.during-minutes:10}")
    public void setDuringMinutes(int value) {
        duringMinutes = value;
    }

    public static String getMessage() {
        return message;
    }

    @Value("${monitoring.es.search.message:}")
    public void setMessage(String value) {
        message = value;
    }

    public static String getServices() {
        return services;
    }

    @Value("${monitoring.es.search.services:}")
    public void setServices(String value) {
        services = value;
    }

    public static String getExcludeMessages() {
        return excludeMessages;
    }

    @Value("${monitoring.es.search.exclude-messages:}")
    public void setExcludeMessages(String value) {
        excludeMessages = value;
    }

    public static String getViewPageUrl() {
        return viewPageUrl;
    }

    @Value("${monitoring.es.search.view-page-url:}")
    public void setViewPageUrl(String value) {
        viewPageUrl = value;
    }
}
