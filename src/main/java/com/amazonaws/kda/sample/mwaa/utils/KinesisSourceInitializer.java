package com.amazonaws.kda.sample.mwaa.utils;

import com.amazonaws.kda.sample.mwaa.model.MwaaLogEvent;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.AWSConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class KinesisSourceInitializer {

    private static final Logger logger = LoggerFactory.getLogger(KinesisSourceInitializer.class);
    public static Properties getKinesisConsumerConfig(ParameterTool paramTool) throws IOException {
        AppProperties appProps = new AppProperties();
        final String region = appProps.getProperty("region");
        Properties kinesisConsumerConfig = new Properties();
        kinesisConsumerConfig.setProperty(AWSConfigConstants.AWS_REGION, paramTool.get("Region", region));
        kinesisConsumerConfig.setProperty(AWSConfigConstants.AWS_CREDENTIALS_PROVIDER, "AUTO");
        kinesisConsumerConfig.setProperty("flink.shard.getrecords.intervalmillis", "1000");
        kinesisConsumerConfig.setProperty("flink.stream.initpos", "LATEST");
        return kinesisConsumerConfig;
    }

    public static DataStreamSource<MwaaLogEvent> getKinesisStream(StreamExecutionEnvironment env,
                                                                  Properties kinesisConsumerConfig,
                                                                  String streamName) {
        return env.addSource(new FlinkKinesisConsumer<MwaaLogEvent>(streamName,
                new JsonDeserializationSchema(),
                kinesisConsumerConfig));
    }

}
