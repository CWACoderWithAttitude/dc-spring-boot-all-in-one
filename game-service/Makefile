clean:
	./mvnw clean
build:
	mvn package
run:
	./mvnw spring-boot:run
debug:
	./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"
