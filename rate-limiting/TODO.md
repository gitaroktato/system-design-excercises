- [x] Faster RPC calls (thread safety)
- [x] Response time distribution
- [x] Multi-module with worker metrics.
- [x] 5 workers benchmark
- [ ] single loop benchmark
- [x] Timer with apiKey https://ktor.io/docs/micrometer-metrics.html#timers
- [x] Keys from 1-8
- [x] Add all endpoints to README


## Optional
- [ ] k6 remote write to Prometheus - https://k6.io/docs/results-output/real-time/prometheus-remote-write/
- [x] Proper logging
- [x] Get rid of println
- [ ] Dynamic API key (service discovery)
- [ ] Add metrics for the IDE as well.
  https://stackoverflow.com/a/66504604/8321787
  https://learn.microsoft.com/en-us/windows/wsl/wsl-config

# STEPS
`wsl ping google.com`
`ifconfig`
`docker login`
`docker compose up -d`
`src/main/docker/dynamodb/init.sh`
`docker compose --profile perf up -d`
`docker compose logs -f worker-1`