#!/bin/bash
git add . && git commit -m deploy
git push origin --all
mvn clean install org.apache.maven.plugins:maven-source-plugin:jar org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
