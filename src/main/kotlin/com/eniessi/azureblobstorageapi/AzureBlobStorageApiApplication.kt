package com.eniessi.azureblobstorageapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AzureBlobStorageApiApplication

fun main(args: Array<String>) {
    runApplication<AzureBlobStorageApiApplication>(*args)
}
