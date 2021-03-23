# Examples for system design excercises


# TinyURL

## High-level estimates

Assuming 500 million new URLs per month and 100:1 read:write ratio, following is the summary of the high level estimates for our service:

|New URLs|	200/s|
|URL redirections|	20K/s|
|Incoming data|	100KB/s|
|Outgoing data|	10MB/s|
|Storage for 5 years|	15TB|
|Memory for cache|	170GB|


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