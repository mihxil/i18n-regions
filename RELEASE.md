

```bash
#mvn release:branch -DbranchName=0.1-SNAPSHOT -DdevelopmentVersion=0.2-SNAPSHOT
#git checkout 0.1-SNAPSHOT
mvn -Pdeploy release:prepare release:perform -DreleaseVersion=0.1 -DdevelopmentVersion=0.2-SNAPSHOT
```
