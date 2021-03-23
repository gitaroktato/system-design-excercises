
# TinyURL

## High-level estimates

Assuming 500 million new URLs per month and 100:1 read:write ratio.

|   Requirement  | Measure    |
| --- | --- |
|New URLs|	200/s|
|URL redirections|	20K/s|
|Incoming data|	100KB/s|
|Outgoing data|	10MB/s|
|Storage for 5 years|	15TB|
|Memory for cache|	170GB|


## Hash Operations Benchmark
The benchmark can be executed with
```shell
./gradlew clean build
java -jar build/libs/tinyurl-0.0.1-SNAPSHOT-jmh.jar UrlShortenerBenchmark -f 1
```

Results
```
Result "com.example.tinyurl.shortening.domain.UrlShortenerBenchmark.benchmarkShorten":
  383966.770 ±(99.9%) 128121.616 ops/s [Average]
  (min, avg, max) = (352546.670, 383966.770, 433866.230), stdev = 33272.777
  CI (99.9%): [255845.154, 512088.387] (assumes normal distribution)
```

## References and Docs

### Riak
https://docs.riak.com/riak/kv/2.2.3/developing/getting-started/java/index.html

### Reactive Spring and RSocket
https://projectreactor.io/
https://rsocket.io/
https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/spring-boot-features.html#boot-features-webflux

### Java Microbenchmark Harness
https://github.com/openjdk/jmh
https://github.com/melix/jmh-gradle-plugin

### API specification
https://springdoc.org/