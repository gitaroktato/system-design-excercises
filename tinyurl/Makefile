# Traefik endpoint is 192.168.99.100
DOCKER_IP = 192.168.99.100:10000/v1/url
# Values are: resolve-test, shorten-test
SCENARIO = resolve-test

TAURUS_COMMAND = bzt \
	-o settings.artifacts-dir=e2e/logs \
	-o settings.env.BASE_URL=$(DOCKER_IP) \
	-o settings.env.BASE_URL=$(DOCKER_IP) \
	-o execution.0.scenario=$(SCENARIO) e2e/taurus.yml

.PHONY: gradle e2e

build: docker compose

gradle:
	bash -c "./gradlew build"

docker: gradle
	bash -c "./gradlew jibDockerBuild -Dhttp.proxyPort=0 -Dhttps.proxyPort=0"

compose:
	docker-compose up -d --force

clean:
	bash -c "./gradlew clean" && docker-compose down

get-hashes:
	bash -c "curl http://${DOCKER_IP}:8098/types/tinyurl/buckets/url/keys?keys=true | sed 's/{\"keys\":\[//g' | sed 's/]}//g' | sed 's/,/,\n/g' > e2e/hashes.csv"

e2e: get-hashes
	$(TAURUS_COMMAND)