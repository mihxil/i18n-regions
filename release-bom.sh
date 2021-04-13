#!/usr/bin/env bash

export NS=http://maven.apache.org/POM/4.0.0
BOM_VERSION=`xmllint  --noout  --shell   i18n-regions-bom/pom.xml << EOF |  grep -E -v '^(\/|text).*' | sed s/-SNAPSHOT/.0/
 setns x=$NS
 cd //x:project/x:version/text()
 cat .
EOF
`
read -p "bom version ($BOM_VERSION): " user_bom
echo $user_bom
if [ ! -z "$user_bom" ] ; then
  BOM_VERSION=$user_bom
fi
export VERSION=${BOM_VERSION}

read -p "version ($VERSION): " user_version
if [ ! -z "$user_version" ] ; then
  VERSION=$user_version
fi
echo $BOM_VERSION / $VERSION
xmllint --shell i18n-regions-bom/pom.xml << EOF > /dev/null
setns x=$NS
cd /x:project/x:version
set ${BOM_VERSION}
cd /x:project/x:properties/x:i18n-regions.version
set ${VERSION}
save
EOF

git diff  i18n-regions-bom/pom.xml


