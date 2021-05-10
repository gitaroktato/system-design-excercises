# Values are: resolve-test, shorten-test
SCENARIO = resolve-test
# Envoy endpoint is 192.168.99.100:10000
# Traefik endpoint is 192.168.99.100
BASE_URL = 192.168.99.100:8080

TAURUS_COMMAND = bzt \
	-o settings.artifacts-dir=e2e/logs \
	-o settings.env.BASE_URL=$(BASE_URL) \
	-o settings.env.BASE_URL=$(BASE_URL) \
	-o execution.0.scenario=$(SCENARIO) e2e/taurus.yml

.PHONY: gradle e2e

build: docker compose

gradle:
	bash -c "./gradlew build"

docker: gradle
	bash -c "./gradlew jibDockerBuild -Dhttp.proxyPort=0 -Dhttps.proxyPort=0"

compose:
	docker-compose up -d --force

e2e:
	$(TAURUS_COMMAND)