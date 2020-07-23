#!/bin/sh

echo "building maven project...."
set -e -x

cd source-code
  ./mvn clean install
cd ..

cp source-code/target/*.war  build-output/.