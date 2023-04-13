package com.example.interaction

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.endpoints.EndpointProvider
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.smithy.kotlin.runtime.http.Url
import aws.smithy.kotlin.runtime.http.endpoints.Endpoint

object DynamoDb {

    val client: DynamoDbClient

    init {

        val credentials = StaticCredentialsProvider.Builder().apply {
            accessKeyId = "DUMMYIDEXAMPLE"
            secretAccessKey = "DUMMYIDEXAMPLE"
        }.build()

        val endpoint = EndpointProvider { Endpoint(Url.parse("http://localhost:8000")) }

        client = DynamoDbClient {
            region = "us-west-2"
            endpointProvider = endpoint
            credentialsProvider = credentials
        }
    }

    suspend fun getValueForKey(tableNameVal: String, keyName: String, keyVal: String): String? {

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet[keyName] = AttributeValue.S(keyVal)

        val request = GetItemRequest {
            key = keyToGet
            tableName = tableNameVal
        }

        val returnedItem = client.getItem(request)
        val result = returnedItem.item
        return result?.get("value")?.asS()
    }
}


