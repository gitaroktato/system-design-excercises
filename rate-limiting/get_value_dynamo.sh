#!/bin/bash
AWS_SECRET_ACCESS_KEY=DUMMYIDEXAMPLE
AWS_ACCESS_KEY_ID=DUMMYIDEXAMPLE
AWS_DEFAULT_REGION=us-west-2

aws dynamodb get-item \
    --table-name key_values \
    --key '{"key": {"S": "key1"}}' \
    --endpoint-url http://localhost:8000