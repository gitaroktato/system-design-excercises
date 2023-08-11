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
- 1 billion active users, 2.4 million searches every minute
- Can use any cloud provider, don't have to be Google Cloud specific.
- Only English support, lowercase and uppercase letters should behave the same
- Inappropriate bot activities, amplifying attacks, DDoS and such should be filtered out to prevent capacity loss.

## High-Level Estimations


### Query payload
### Response payload

## Technology Choices
- GRPC for reduced latency and higher throughput
- React components for being able to import search bar to other projects
