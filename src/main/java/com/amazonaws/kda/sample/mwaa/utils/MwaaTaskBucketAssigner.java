package com.amazonaws.kda.sample.mwaa.utils;

import com.amazonaws.kda.sample.mwaa.model.MwaaLogEvent;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;

public class MwaaTaskBucketAssigner extends DateTimeBucketAssigner<MwaaLogEvent> {
    @Override
    public String getBucketId(MwaaLogEvent element, Context context) {
        String bucketId = super.getBucketId(element, context);
        return bucketId+"/"+element.getIngestionTime();
    }
}
