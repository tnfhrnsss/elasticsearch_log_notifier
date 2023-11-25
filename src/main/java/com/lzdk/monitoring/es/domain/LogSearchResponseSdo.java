package com.lzdk.monitoring.es.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lzdk.monitoring.utils.json.JsonSerializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogSearchResponseSdo implements JsonSerializable {
    private String index;

    private String logId;

    private String service;

    private String message;

    private String stackMessage;

    private String timeStamp;

    @Override
    public String toString() {
        return toJson();
    }
}
