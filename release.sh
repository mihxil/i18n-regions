#!/usr/bin/env bash

export NS=http://maven.apache.org/POM/4.0.0
TARGET_VERSION=`xmllint  --noout  --shell  i18n-regions-bom/pom.xml << EOF |  grep -E -v '^(\/|text).*'
 setns x=$NS
 cd //x:project/x:properties/x:i18n-regions.version/text()
 cat .
EOF
`
SNAPSHOT_VERSION=`xmllint  --noout  --shell  pom.xml << EOF |  grep -E -v '^(\/|text).*'
 setns x=$NS
 cd //x:project/x:version/text()
 cat .
EOF
`

DEVELOPMENT_VERSION=`echo $SNAPSHOT_VERSION | awk -F[.-] '{print $1"."$2+1"-"$3}'`
BRANCH_DEVELOPMENT_VERSION=`echo $TARGET_VERSION | awk -F[.-] '{print $1"."$2"."$3+1"-SNAPSHOT"}'`
echo "$SNAPSHOT_VERSION -> $DEVELOPMENT_VERSION, $TARGET_VERSION, $BRANCH_DEVELOPMENT_VERSION"
mvn release:branch -DbranchName=$SNAPSHOT_VERSION -DdevelopmentVersion=$DEVELOPMENT_VERSION
git checkout SNAPSHOT_VERSION
git branch -l
echo mvn -Pdeploy release:prepare release:perform -DreleaseVersion=$TARGET_VERSION -DdevelopmentVersion=1.1.1-SNAPSHOT


