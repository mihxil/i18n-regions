
.PHONY: simple.result boot

simple.result:
		docker run   -v ./tests/simple:/mnt -v ~/.m2/repository:/root/.m2/repository -w /mnt  maven:3.9-eclipse-temurin-21-alpine mvn -q | tee  $@

boot:
		docker run  -p 8080:8080 -v ./tests/springboot-java11:/mnt -v ~/.m2/repository:/root/.m2/repository -w /mnt  maven:3.9-eclipse-temurin-21-alpine mvn -q
