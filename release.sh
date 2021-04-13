#!/usr/bin/env bash

# get new version from bom
export NS=http://maven.apache.org/POM/4.0.0
TARGET_VERSION=`xmllint  --noout  --shell  i18n-regions-bom/pom.xml << EOF |  grep -E -v '^(\/|text).*'
 setns x=$NS
 cd //x:project/x:properties/x:i18n-regions.version/text()
 cat .
EOF
`

# determin current version
SNAPSHOT_VERSION=`xmllint  --noout  --shell  pom.xml << EOF |  grep -E -v '^(\/|text).*'
 setns x=$NS
 cd //x:project/x:version/text()
 cat .
EOF
`
# now, for the new development version we increase minor by one
DEVELOPMENT_VERSION=`echo $SNAPSHOT_VERSION | awk -F[.-] '{print $1"."$2+1"-"$3}'`
# and in the branch will increase the patch version by one
BRANCH_DEVELOPMENT_VERSION=`echo $TARGET_VERSION | awk -F[.-] '{print $1"."$2"."$3+1"-SNAPSHOT"}'`
echo "$SNAPSHOT_VERSION -> $DEVELOPMENT_VERSION, $TARGET_VERSION, $BRANCH_DEVELOPMENT_VERSION"

# we use maven to make the branch
mvn release:branch -DbranchName=$SNAPSHOT_VERSION -DdevelopmentVersion=$DEVELOPMENT_VERSION
git checkout $SNAPSHOT_VERSION
# show what happened:
git branch -l
# this command will make and deploy the actual release.
# echo it for review, it then can be copy/pasted to execute
echo mvn -Pdeploy release:prepare release:perform -DreleaseVersion=$TARGET_VERSION -DdevelopmentVersion=$BRANCH_DEVELOPMENT_VERSION


