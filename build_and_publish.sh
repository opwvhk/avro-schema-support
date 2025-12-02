#!/bin/zsh
if [[ "$(grep "^version" build.gradle.kts)" == *SNAPSHOT* ]]
then
  echo "Publishing SNAPSHOT versions is not supported"
  exit 1
fi

./update_until_build.sh
./gradlew clean buildPlugin test verifyPlugin publishPlugin
cp -v ./build/distributions/*.zip ../archive-avro-schema-support/
