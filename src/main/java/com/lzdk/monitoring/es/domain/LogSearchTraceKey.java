package com.lzdk.monitoring.es.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lzdk.monitoring.utils.json.JsonSerializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogSearchTraceKey implements JsonSerializable {
    private String index;

    private String logId;

    private String traceId;

    @Override
    public String toString() {
        return toJson();
    }

    @JsonIgnore
    public boolean traceByLogId() {
        return StringUtils.isEmpty(this.traceId);
    }
}
