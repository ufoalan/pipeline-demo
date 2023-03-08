#!/bin/bash
CHART_NAME=$1
CHART_VERSION=$2
echo "hello $CHART_NAME $CHART_VERSION"
helm create $CHART_NAME
# replace the image name by your own image name
sed -i .bak "s/repository: nginx/repository: $CHART_NAME/g" $CHART_NAME/values.yaml
sed -i .bak 's/port: 80/port: 8080/g' $CHART_NAME/values.yaml
sed -i .bak "s/1.16.0/$CHART_VERSION/g" $CHART_NAME/Chart.yaml
helm template $CHART_NAME
