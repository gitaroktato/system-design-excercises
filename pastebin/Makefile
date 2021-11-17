# Traefik endpoint is 192.168.99.100
DOCKER_IP = 192.168.99.100

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
