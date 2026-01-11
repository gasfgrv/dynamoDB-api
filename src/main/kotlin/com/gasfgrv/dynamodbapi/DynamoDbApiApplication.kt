package com.gasfgrv.dynamodbapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DynamoDbApiApplication

fun main(args: Array<String>) {
	runApplication<DynamoDbApiApplication>(*args)
}
