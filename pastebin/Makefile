DOCKER_IP = 192.168.99.100:9080
SCENARIO = paste-read-test
TAURUS_COMMAND = bzt \
	-o settings.artifacts-dir=e2e/logs \
	-o settings.env.BASE_URL=$(DOCKER_IP) \
	-o execution.0.scenario=$(SCENARIO) e2e/taurus.yml

CURRENT_DIR = $(shell pwd)

.PHONY: gradle e2e

build: docker compose

gradle:
	bash -c "./gradlew build"

docker: gradle
	bash -c "./gradlew jibDockerBuild -Dhttp.proxyPort=0 -Dhttps.proxyPort=0"

compose:
	docker-compose up -d --force

clean:
	bash -c "./gradlew clean" && docker-compose down --volumes

e2e:
	$(TAURUS_COMMAND)

e2e-container:
	docker run -it --rm --network pastebin_apisix -v $(CURRENT_DIR)/e2e:/bzt-configs blazemeter/taurus \
	-o modules.jmeter.gui=false \
	-o settings.env.BASE_URL=apisix:9080 taurus.yml
