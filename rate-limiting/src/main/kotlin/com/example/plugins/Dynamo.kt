package com.example.plugins

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.smithy.kotlin.runtime.auth.awssigning.AwsSigner
import aws.smithy.kotlin.runtime.http.Url

suspend fun getValueForKey(tableNameVal: String, keyName: String, keyVal: String) {

    val keyToGet = mutableMapOf<String, AttributeValue>()
    keyToGet[keyName] = AttributeValue.S(keyVal)

    val request = GetItemRequest {
        key = keyToGet
        tableName = tableNameVal
    }
    val provider = StaticCredentialsProvider.Builder().apply {
        accessKeyId = "fakeMyKeyId"
        secretAccessKey = "fakeSecretAccessKey"
    }.build()

    DynamoDbClient {
        credentialsProvider = provider
        region = "us-east-1"
        endpointUrl = Url.parse("http://localhost:8000")
    }.use { ddb ->
        val returnedItem = ddb.getItem(request)
        val numbersMap = returnedItem.item
        numbersMap?.forEach { entry ->
            println(entry.key)
            println(entry.value)
        }
    }
}

