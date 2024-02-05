# Typeahead

## High-Level Requirements
Provide typeahead functionality to a system similar to Google

### Functional Requirements
When a user types in a few characters, a suggestion list should appear based on the top 5 terms prefixed with these characters.
- We don't need to consider the search history of the user
- The top 5 phrases should be returned considering global usage

### Non-Functional Requirements
- Latency-Sensitive - response time higher, than 1.2 seconds is considered a failure
    - p99 latency should be under 175ms
    - p99.9 latency should be under 200ms
    - p100 latency should be under 1.2s

- Scalable - global user space from all over the world
- Highly available - Target availability is 99.99%
- Fault-tolerant - Search should still work uninterrupted if suggestions are unavailable
- 1 billion active users, 2.4 million searches every minute (from [here](https://firstsiteguide.com/google-search-stats/))
- Can use any cloud provider, it doesn't have to be Google Cloud specific.
- Only English support, lowercase and uppercase letters should behave the same
- Inappropriate bot activities, amplifying attacks, DDoS and such should be filtered out to prevent capacity loss.

## High-Level Estimations

### Query payload
The query should send a minimum set of information, more or less:
- Prefix (up to a limited set of characters, 5-6 keystrokes)
- Rate-limiting can be done based on hashing various values of the payload, like client IP, User-Agent, etc. No need for a predefined API key. 
We can assume, that each query payload is around 5 bytes + headers. Roughly 200 bytes. This means 480 MB / min of data transfer on the client side.


### Response payload
The response contains the top 5 suggestions, which is an average sentence of 5-6 words. 
![Example from Google](docs/image.png)

That's 30 characters on average for a sentence, which adds up to 30*5 bytes + headers. We can roughly calculate 3x the size of the requests (1.4 GB / min).

## Overall Design Decisions

### Read Path

### Write Path


## Technology Choices
- GRPC/WebSocket/SSE for reduced latency and higher throughput?
- React components for being able to import search bar to other projects



# References & Links
https://adjoe.io/employee-story/working-with-grpc-web/