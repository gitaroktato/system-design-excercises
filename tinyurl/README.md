# TinyURL

## Functional Requirements
* Shortened URLs should be unique
* Expiration should be considered for shortened URLs
* A custom text can be picked for shortened URLs

## Non-functional Requirements
_Performance_
* The URL resolution should happen near real-time.
* Reading and creating shortened URLs (read/write) should be separated
* URL resolution should be cached, but warmup period should not affect overall read performance

_Availability_
* There shouldn't be any single point of failure for any component
* System should tolerate failure of writes/reads independently

_Scalability_
* Every component should be individually horizontally scalable
* Reading and creating shortened URLs (read/write) should be independently scalable

_Observability_
The following data should be measured and collected:
- New URLs / second
- URL redirections / second
- Storage capacity
- Memory usage / application
- Memory usage / distributed cache

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

## API

OpenAPI documentation is available within the [open-api.yaml](open-api.yaml) file.

## Data Model


## Hash Operations Benchmark
The benchmark can be executed with
```shell
./gradlew clean build
java -jar build/libs/tinyurl-0.0.1-SNAPSHOT-jmh.jar UrlShortenerBenchmark -f 1
```

Results
```
Result "com.example.tinyurl.shortening.service.UrlShortenerBenchmark.benchmarkShorten":
  383966.770 ±(99.9%) 128121.616 ops/s [Average]
  (min, avg, max) = (352546.670, 383966.770, 433866.230), stdev = 33272.777
  CI (99.9%): [255845.154, 512088.387] (assumes normal distribution)
```

## Caching


## References and Docs

### Riak
https://docs.riak.com/riak/kv/2.2.3/developing/getting-started/java/index.html

### Reactive Spring and RSocket
https://rsocket.io/
https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/spring-boot-features.html#boot-features-webflux
https://docs.spring.io/spring-framework/docs/5.3.5/reference/html/web-reactive.html#spring-webflux
https://projectreactor.io/docs/core/release/reference
https://www.codota.com/code/java/methods/reactor.test.StepVerifier$Step/expectErrorSatisfies

### Java Microbenchmark Harness
https://github.com/openjdk/jmh
https://github.com/melix/jmh-gradle-plugin

### API specification
https://springdoc.org/

### AssertJ and testing
https://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html