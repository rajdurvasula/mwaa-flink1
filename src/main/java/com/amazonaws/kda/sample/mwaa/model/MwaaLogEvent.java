package com.amazonaws.kda.sample.mwaa.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MwaaLogEvent {

    private Long timestamp;
    private String message;
    private Long ingestionTime;

    @JsonGetter("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonSetter("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonGetter("ingestionTime")
    public Long getIngestionTime() {
        return ingestionTime;
    }

    @JsonSetter("ingestionTime")
    public void setIngestionTime(Long ingestionTime) {
        this.ingestionTime = ingestionTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp: ").append(timestamp).append("\n")
                .append("Message: ").append(message).append("\n")
                .append("IngestionTime: ").append(ingestionTime);
        return sb.toString();
    }
}
