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

build:
	docker build -t $(tag) .

run: build
	docker run --rm -it $(tag)

asciidoc2docbook:
	docker run --rm -v $(PWD):/documents asciidoctor/docker-asciidoctor asciidoctor -b docbook5 $(asciidoc_input)
	docker run --rm -v $(PWD):/documents asciidoctor/docker-asciidoctor asciidoctor -b docbook5 article.adoc -o article.xml

clean:
	rm -f $(asciidoc_output) $(markdown_result) article.xml article.md

docbook2md:
	#docker run --rm -v $(PWD):/data pandoc/core -f docbook -t markdown -o README.md README.xml
	docker run --rm -v $(PWD):/data pandoc/core -f docbook -t markdown -o $(markdown_result) $(asciidoc_output)
	docker run --rm -v $(PWD):/data pandoc/core -f docbook -t markdown -o article.md article.xml


generate_markdown: clean asciidoc2docbook docbook2md


