#!/bin/bash


repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:repl Ripley.core/process \
    :main-ns Lang.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Lang.main
}

ui(){
  # watch release clj-repl
  npm i --no-package-lock
  mkdir -p out/ui/
  cp src/Lang/index.html out/ui/index.html
  clj -A:Moana:ui -M -m shadow.cljs.devtools.cli $1 ui
  # (shadow/watch :ui)
  # (shadow/repl :ui)
  # :repl/quit
}

ui_release(){
  rm -rf out/ui
  ui release
}

tag(){
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  TAG="$COMMIT_COUNT-$COMMIT_HASH"
  git tag $TAG $COMMIT_HASH
  echo $COMMIT_HASH
  echo $TAG
}

jar(){

  clojure \
    -X:identicon Zazu.core/process \
    :word '"Lang"' \
    :filename '"out/identicon/icon.png"' \
    :size 256

  rm -rf out/*.jar
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  clojure \
    -X:uberjar Genie.core/process \
    :main-ns Lang.main \
    :filename "\"out/Lang-$COMMIT_COUNT-$COMMIT_HASH.jar\"" \
    :paths '["src" "out/ui"]'
}

release(){
  jar
}

"$@"