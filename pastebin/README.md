# Pastebin

## Functional Requirements
* Users can upload text and get a unique URL
* Pastes should expire over time. Expiration can be customized
* A paste can be additionally protected with a specified password
* A custom URL alias can be picked for the paste if it's not taken

## Non-functional requirements

_Consistency_
* Each paste should have an unique ID assigned.
* We don't have a limitation on the ID's length. It can be shortened if required (the solution is identical to the previous excercise).

_Performance_
* Reading and creating pastes (read/write) should be performing differently based on different read/write performance requirements.
* Popular pastes should be cached, but warmup period should not affect overall read performance.

_Scalability_
* Every component should be individually horizontally scalable.
* Reading and creating pastes (read/write) should be independently scalable.

_Reliability_
* There shouldn't be any single point of failure for any component.
* System should tolerate failure of writes/reads independently.

_Security_
- Paste URLs should not be guessed by unauthorized users by a simple enumeration. 

_Observability_
The following data should be measured and collected:
- New pastes / second
- Paste reads / second
- Storage capacity
- Memory usage / application
- Memory usage / cache

_Auditability_
- We should collect popularity of each paste for further analysis and collecting usage statistics.
- A specific user is a power-user if that person's average paste reaches 1,000,000 views. 

## Capacity Estimations
Assuming 25 million new pastes per month and 10:1 read:write ratio.
Assuming, that the maximum size of a paste can be 5 MB.
Assuming the average size of a paste is not more than 25 KB.
We should store each paste for 2 years at maximum.
We can use 80:20 rule for caching capacity.

|   Requirement  | Measure    |
| --- | --- |
|New pastes|	9.64/s|
|Paste reads|	96.4/s|
|Maximum incoming data|	48.2 MB/s|
|Maximum outgoing data|	482 MB/s|
|Average incoming data|	241 KB/s|
|Average outgoing data|	2.4 MB/s|
|Storage for 2 years|	15 TB|
|Memory for cache|	4.1 GB|

## API Design
OpenAPI documentation is available in the [open-api.yaml](open-api.yaml) file.