package com.example.plugins

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.endpoints.EndpointProvider
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.smithy.kotlin.runtime.http.Url
import aws.smithy.kotlin.runtime.http.endpoints.Endpoint

suspend fun getValueForKey(tableNameVal: String, keyName: String, keyVal: String): String? {

    val keyToGet = mutableMapOf<String, AttributeValue>()
    keyToGet[keyName] = AttributeValue.S(keyVal)

    val request = GetItemRequest {
        key = keyToGet
        tableName = tableNameVal
    }

    val endpoint = EndpointProvider { Endpoint(Url.parse("http://localhost:8000")) }

    DynamoDbClient {
        region = "us-west-2"
        endpointProvider = endpoint
    }.use { ddb ->
        val returnedItem = ddb.getItem(request)
        val result = returnedItem.item
        result?.forEach { entry ->
            println(entry.key)
            println(entry.value)
        }
        return result?.get("value")?.asS()
    }
}

