package com.amazonaws.kda.sample.mwaa;

import com.amazonaws.kda.sample.mwaa.model.MwaaLogEvent;
import com.amazonaws.kda.sample.mwaa.operators.PartitionPreloadExtractData;
import com.amazonaws.kda.sample.mwaa.utils.AppProperties;
import com.amazonaws.kda.sample.mwaa.utils.KinesisSourceInitializer;
import com.amazonaws.kda.sample.mwaa.utils.MwaaTaskBucketAssigner;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class S3StreamingSinkJob
{
    private static final Logger logger = LoggerFactory.getLogger(S3StreamingSinkJob.class);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        AppProperties properties = new AppProperties();
        ParameterTool paramTool = ParameterTool.fromArgs(args);
        final String dataStream = properties.getProperty("kinesis.stream");
        Properties kinesisConsumerConfig = KinesisSourceInitializer.getKinesisConsumerConfig(paramTool);
        DataStream<MwaaLogEvent> taskStream = KinesisSourceInitializer.getKinesisStream(env,
                kinesisConsumerConfig,
                dataStream);
        DataStream<MwaaLogEvent> ingestRecords = taskStream
                .filter(record -> MwaaLogEvent.class.isAssignableFrom(record.getClass()))
                .keyBy(record -> record.getIngestionTime())
                .flatMap(new PartitionPreloadExtractData());

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        String s3pattern = "s3://%s/%s/";
        String s3BucketPrefix = formatter.format(s3pattern, properties.getProperty("s3.bucket"), "data")
                .toString();
                //"s3://"+properties.getProperty("s3.bucket")+"/data/";
        StreamingFileSink<MwaaLogEvent> s3Sink = StreamingFileSink
                .forRowFormat(new Path(s3BucketPrefix),
                        (MwaaLogEvent event, OutputStream ostream) -> {
                            PrintStream out = new PrintStream(ostream);
                            out.println(event.toString());
                        }).withBucketAssigner(new MwaaTaskBucketAssigner())
                        .build();
        ingestRecords.addSink(s3Sink).name("sso-del-test");
        logger.info("Reading events from Stream: {}", paramTool.get("InputStreamName", dataStream));
        env.execute();
    }

}
