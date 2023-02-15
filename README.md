# mwaa-flink1

Sample AWS Kinesis Data Analytics (`KDA`) for Apache Flink project to:
- read from Kinesis Data Stream: `Refer: config.properties`
- create Flink KeyedStream by `ingestionTime` field from Event Record
- Filter out Event Records that has specific text in `message` field in Event Record
- Write Event Records in KeyedStream to S3 Bucket

## Boto3 script to read Log Events
- Boto3 code reads:
- Log Events from Log Stream
- Puts Records into Kinesis Data Stream

## Flink Application
- Maven Project to read Event Records from Kinesis Data Stream
- Filter specific Event Records
- Write to S3 Bucket

## Setup AWS resources
### IAM
- Create IAM Role for KDA with Trust Relationship Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Service": "kinesisanalytics.amazonaws.com"
            },
            "Action": "sts:AssumeRole"
        }
    ]
}
```
- Attach CloudWatch Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "autoscaling:Describe*",
                "cloudwatch:*",
                "logs:*",
                "sns:*",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:GetRole"
            ],
            "Effect": "Allow",
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": "iam:CreateServiceLinkedRole",
            "Resource": "arn:aws:iam::*:role/aws-service-role/events.amazonaws.com/AWSServiceRoleForCloudWatchEvents*",
            "Condition": {
                "StringLike": {
                    "iam:AWSServiceName": "events.amazonaws.com"
                }
            }
        }
    ]
}
```
- Attach CW Logs Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "logs:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
```
- Attach Kinesis Data Stream Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "kinesis:*",
            "Resource": "*"
        }
    ]
}
```
- Attach S3 Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": "*"
        }
    ]
}
```

### Kinesis Data Stream
- Create Kinesis Data Stream
```
aws kinesis create-stream --stream-name mwaa-test-stream --shard-count 1
```

### Create S3 Bucket for Flink Code binary
- Create S3 Bucket and upload Flink `Fat` .JAR to Bucket


## How to run
- Setup Kinesis Data Stream
- Create Kinesis Data Analytics (`KDA`) Application for Apache Flink
- Configure KDA
- Select IAM Role created for KDA
- Specify S3 Bucket and Key to Flink Binary `Fat` .JAR
- Run KDA
- Run Boto3 script to read Log Group, Log Stream that has Amazon MWAA Task Log records


## Verify
- Open Flink Console from KDA to see the Job processing event records
- Check S3 Bucket Prefix `s3://<bucket>/data` for processed records stored as files
