#!/bin/bash
AWS_SECRET_ACCESS_KEY=DUMMYIDEXAMPLE
AWS_ACCESS_KEY_ID=DUMMYIDEXAMPLE
AWS_DEFAULT_REGION=us-west-2

aws dynamodb list-tables --endpoint-url http://localhost:8000
if [ $? -eq 0 ]; then
    echo "DynamoDB started, creating tables..."
    aws dynamodb create-table \
          --table-name key_values \
          --attribute-definitions AttributeName=key,AttributeType=S \
          --key-schema AttributeName=key,KeyType=HASH \
          --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
          --endpoint-url http://localhost:8000

    echo "Uploading data..."
    for FILE in $(dirname -- "$0")/data/*.json;
    do
      echo "Processing batch file $FILE"
      aws dynamodb batch-write-item --endpoint-url http://localhost:8000 --request-items file://$FILE
    done
    echo "Data upload finished"
    aws dynamodb list-tables --endpoint-url http://localhost:8000
else
    echo "ERROR, DynamoDB not available"
    exit -1
fi
