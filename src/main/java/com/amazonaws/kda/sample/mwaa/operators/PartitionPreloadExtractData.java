package com.amazonaws.kda.sample.mwaa.operators;

import com.amazonaws.kda.sample.mwaa.model.MwaaLogEvent;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

public class PartitionPreloadExtractData extends RichFlatMapFunction<MwaaLogEvent, MwaaLogEvent> {

    @Override
    public void flatMap(MwaaLogEvent mwaaLogEvent, Collector<MwaaLogEvent> collector) throws Exception {
        if (!mwaaLogEvent.getMessage().contains("---") ||
        !mwaaLogEvent.getMessage().contains("   ")) {
            collector.collect(mwaaLogEvent);
        }
    }
}
