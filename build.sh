#!/bin/bash
  buildNumber=`echo $TRAVIS_JOB_NUMBER | cut -d"." -f1` 
  buildDate=`date +%Y%m%d`
if [ "$TRAVIS_BRANCH" = "ui_integrator" ] ; then
  mvn clean install -DforceContextQualifier=$buildDate-$buildNumber -f hydrograph.ui/pom.xml
elif [ "$TRAVIS_BRANCH" = "engine_integrator" ] ; then
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle --info --stacktrace
elif [ "$TRAVIS_BRANCH" = "integrator" ] ; then
  mvn clean install -DforceContextQualifier=$buildDate-$buildNumber -f hydrograph.ui/pom.xml
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
elif [ "$TRAVIS_BRANCH" = "master" ] ; then
  mvn clean install -DforceContextQualifier=$buildDate-$buildNumber -f hydrograph.ui/pom.xml
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
fi
