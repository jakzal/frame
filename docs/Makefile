default: build

build:
	@docker run -t --rm --name docs -v ${CURDIR}:/docs -w /docs jakzal/asciidoctor bin/build.sh
.PHONY: build

clean:
	@rm -rf build/**
.PHONY: clean
