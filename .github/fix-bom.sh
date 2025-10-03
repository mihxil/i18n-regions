#!/usr/bin/env bash
batch='false'
release='cat'
while getopts 'BR' flag; do
    case "${flag}" in
        B) batch=true ;;
        R) release='sed s/-SNAPSHOT//' ;;
    esac
done

NS=http://maven.apache.org/POM/4.0.0


# Determine version of bom to be released
# get from pom.xml
BOM_VERSION=`xmllint  --noout  --shell   pom.xml << EOF |  grep -E -v '^(\/|text).*'  | ${release}
 setns x=$NS
 cd //x:project/x:version/text()
 cat .
EOF
`
if ! ${batch}; then
	# ask user
	read -p "bom version ($BOM_VERSION): " user_bom
	echo $user_bom
	if [ ! -z "$user_bom" ] ; then
	  BOM_VERSION=$user_bom
	fi
fi

# determine version of project to be released
VERSION=${BOM_VERSION}

if ! ${batch}; then
	# ask user
	read -p "version ($VERSION): " user_version
	if [ ! -z "$user_version" ] ; then
	  VERSION=$user_version
	fi
fi
echo $BOM_VERSION / $VERSION

# found, now fill that in the bom's pom.xml
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



