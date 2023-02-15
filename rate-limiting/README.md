# Implementing Backpressure & Rate Limiting with RabbitMQ

## High-Level Requirements

Client requests are arriving at a higher rate, than the [provisioned capacity unit](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.ReadWriteCapacityMode.html#HowItWorks.ProvisionedThroughput.Manual) of the underlying DynamoDB service. For a certain proportion of the requests, the database is starting to throttle and responds with HTTP 400 `ProvisionedThroughputExceededException`.

We would like to implement backpressure on the server side, which slows down the arrival rate to the desired level: If the provisioned RCU for DynamoDB is 200, then the read throughput of the service is 800 reads/sec at maximum.

The implementation should also provide a fair share of capacity for their clients using their API keys. So if one of the clients is misbehaving, it's not going to affect others from getting a response ([noisy neighbor antipattern](https://learn.microsoft.com/en-us/azure/architecture/antipatterns/noisy-neighbor/noisy-neighbor).

The solution should also scale properly:

- New clients can arrive with a previously unseen API key.
- Rate-limiting rules are global: If our services start to scale out, the allowed capacity per API key should remain the same.

![baseline](docs/rate-limiting-reqs.drawio.png)

## Implementation Algorithms and Examples
### [Token Bucket](https://en.wikipedia.org/wiki/Token_bucket)

- [DynamoDB](https://www.youtube.com/watch?v=yvBR71D0nAQ&t=1340s)

- [AWS Rate-Limiting](https://d1.awsstatic.com/builderslibrary/pdfs/fairness-in-multi-tenant-systems-david-yanacek.pdf)

### [Leaky Bucket](https://en.wikipedia.org/wiki/Leaky_bucket)

### Fixed Window Counter 
- [Example with Redis](https://redis.com/redis-best-practices/basic-rate-limiting/)

### Sliding Window Counter & Log
- [Example with Redis](https://redis.com/redis-best-practices/time-series/sorted-set-time-series/)
- [Another Example with Redis](https://engineering.classdojo.com/blog/2015/02/06/rolling-rate-limiter/)


## Design Considerations
### Builtin Rate-Limiter Inside the Service


### Rate-Limiting with Service Mesh

### Rate-Limiting with RabbitMQ

#### Using Queue Length Limits
See https://www.rabbitmq.com/maxlength.html


# References

## DynamoDB Kotlin

https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/kotlin_dynamodb_code_examples.html

https://github.com/bexway/kotlin-spring-dynamo-example/tree/master/src/main/kotlin/com/shoprunner/kotlinspringdynamoexample

## DynamoDB Local

https://hub.docker.com/r/amazon/dynamodb-local

https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.UsageNotes.html

https://github.com/aws-samples/aws-sam-java-rest

## Allow WSL2 to connect to Windows host
https://superuser.com/questions/1535269/how-to-connect-wsl-to-a-windows-localhost

https://superuser.com/questions/1679757/how-to-access-windows-localhost-from-wsl2


## k6 With TypeScript Template

https://k6.io/docs/testing-guides/api-load-testing/

https://github.com/grafana/k6-template-typescript

## RabbitMQ using Kotlin

https://medium.com/swlh/async-messaging-with-kotlin-and-rabbitmq-d69df1937b25

https://www.rabbitmq.com/tutorials/tutorial-six-java.html

https://github.com/rabbitmq/rabbitmq-tutorials/blob/main/kotlin/src/main/kotlin/RPCServer.kt

