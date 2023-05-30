#!/bin/bash
set -eo pipefail

PROJECT_DIR=$(dirname $(readlink -f $0))/../

function build_html() {
  name=$1
  source=$2
  target=$3
  printf '%s' "$name "
  output=$(asciidoctor $source -o $target 2>&1) \
    && echo "✔︎" \
    || echo "✗"
  [[ "$output" -eq "" ]] || echo $output
}

build_html "Diary (HTML)" "docs/diary/src/index.adoc" "docs/build/diary.html"
