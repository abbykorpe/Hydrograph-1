#!/bin/bash

if [ "$TRAVIS_BRANCH" = "ui_integrator" ] ; then
  mvn clean install -f hydrograph.ui/pom.xml
elif [ "$TRAVIS_BRANCH" = "engine_integrator" ] ; then
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
elif [ "$TRAVIS_BRANCH" = "integrator" ] ; then
  mvn clean install -f hydrograph.ui/pom.xml
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
elif [ "$TRAVIS_BRANCH" = "preprod" ] ; then
  mvn clean install -f hydrograph.ui/pom.xml
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
elif [ "$TRAVIS_BRANCH" = "master" ] ; then
  mvn clean install -f hydrograph.ui/pom.xml
  gradle build --build-file hydrograph.engine/build.gradle --settings-file hydrograph.engine/settings.gradle
fi
