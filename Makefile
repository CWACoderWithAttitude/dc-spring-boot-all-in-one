tag=game-service
# https://gitlab.b-data.ch/ghc/ghc4pandoc
pandoc_image=registry.gitlab.b-data.ch/ghc/ghc4pandoc:latest
# https://gitlab.b-data.ch/ghc/ghc-musl
#pandoc_image=docker.io/benz0li/ghc-musl:latest
asciidoc_input=README.adoc
asciidoc_output=README.xml
markdown_result=README.md

.PHONY: build run adoc2md asciidoc xml2md
# Build and run the Docker container for the game service

check_docker_compose_config:
	docker-compose -f .devcontainer/docker-compose.yml config

sb_clean:
	game-service/mvnw clean --file game-service/pom.xml
sb_build:
	game-service/mvnw package --file game-service/pom.xml
sb_run:
	game-service/mvnw spring-boot:run --file game-service/pom.xml
sb_debug:
	game-service/mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000" --file game-service/pom.xml


build:
	docker build -t $(tag) .

run: build
	docker run --rm -it $(tag)

asciidoc2docbook:
	docker run --rm -v $(PWD):/documents asciidoctor/docker-asciidoctor asciidoctor -b docbook5 $(asciidoc_input) -a leveloffset=+1
	docker run --rm -v $(PWD):/documents asciidoctor/docker-asciidoctor asciidoctor -b docbook5 article.adoc -a leveloffset=+1 -o article.xml

clean:
	rm -f $(asciidoc_output) $(markdown_result) article.xml article.md

docbook2md:
	docker run --rm -v $(PWD):/data pandoc/core -f docbook -t markdown --toc -o $(markdown_result) $(asciidoc_output)
	docker run --rm -v $(PWD):/data pandoc/core -f docbook -t markdown --toc -o article.md article.xml

generate_markdown: clean asciidoc2docbook docbook2md


