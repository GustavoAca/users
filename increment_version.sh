# increment_version.sh
#!/bin/bash

current_version=$(xmllint --xpath "//*[local-name()='version']/text()" pom.xml | head -n 1)
IFS='.' read -r -a version_parts <<< "$current_version"
major=${version_parts[0]}
minor=${version_parts[1]}
patch=${version_parts[2]}
new_minor=$((minor + 1))
new_version="$major.$new_minor.$patch"
echo "new_version=$new_version" >> $GITHUB_ENV
mvn versions:set -DnewVersion=$new_version
