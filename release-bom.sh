#!/usr/bin/env bash

NS=http://maven.apache.org/POM/4.0.0

# Determin version of bom to be released

# get from pom.xml
BOM_VERSION=`xmllint  --noout  --shell   i18n-regions-bom/pom.xml << EOF |  grep -E -v '^(\/|text).*' | sed s/-SNAPSHOT/.0/
 setns x=$NS
 cd //x:project/x:version/text()
 cat .
EOF
`
# ask user
read -p "bom version ($BOM_VERSION): " user_bom
echo $user_bom
if [ ! -z "$user_bom" ] ; then
  BOM_VERSION=$user_bom
fi

# determin version of project to be released
VERSION=${BOM_VERSION}

# ask user
read -p "version ($VERSION): " user_version
if [ ! -z "$user_version" ] ; then
  VERSION=$user_version
fi
echo $BOM_VERSION / $VERSION

# found now fill that in the bom's pom.xml
xmllint --shell i18n-regions-bom/pom.xml << EOF > /dev/null
setns x=$NS
cd /x:project/x:version
set ${BOM_VERSION}
cd /x:project/x:properties/x:i18n-regions.version
set ${VERSION}
save
EOF

# show what happens
git diff  i18n-regions-bom/pom.xml

# update version of bom in dependency managment itself
xmllint --shell pom.xml << EOF > /dev/null
setns x=$NS
cd /x:project/x:dependencyManagement/x:dependencies/x:dependency[x:artifactId = 'i18n-regions-bom']/x:version
set ${BOM_VERSION}
save
EOF
git diff  pom.xml



