
.PHONY: gradle

build: docker compose

gradle:
	bash -c "./gradlew build"

docker: gradle
	bash -c "./gradlew jibDockerBuild -Dhttp.proxyPort=0 -Dhttps.proxyPort=0"

compose:
	docker-compose up -d --force