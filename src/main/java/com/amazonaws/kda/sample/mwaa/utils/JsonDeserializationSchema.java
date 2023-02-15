package com.amazonaws.kda.sample.mwaa.utils;

import com.amazonaws.kda.sample.mwaa.model.MwaaLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class JsonDeserializationSchema extends AbstractDeserializationSchema<MwaaLogEvent> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MwaaLogEvent deserialize(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, MwaaLogEvent.class);
    }
}
