aws dynamodb get-item \
    --table-name key_values \
    --key '{"key": {"S": "key1"}}' \
    --endpoint-url http://localhost:8000