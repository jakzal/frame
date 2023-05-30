default: build

build:
	@docker run -t --rm --name docs -v ${CURDIR}:/frame -w /frame jakzal/asciidoctor docs/bin/build.sh
.PHONY: build

clean:
	@rm -rf docs/build/**
.PHONY: clean
