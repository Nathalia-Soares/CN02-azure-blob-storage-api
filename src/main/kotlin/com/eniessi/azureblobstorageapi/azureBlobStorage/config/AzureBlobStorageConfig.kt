package com.eniessi.azureblobstorageapi.azureBlobStorage.config

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AzureBlobStorageConfig {

    @Value("\${azure.storage.connection-string}")
    private lateinit var connectionString: String

    @Value("\${azure.storage.container-name}")
    private lateinit var containerName: String

    @Bean
    fun blobServiceClient(): BlobServiceClient {
        return BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient()
    }

    @Bean
    fun blobContainerClient(blobServiceClient: BlobServiceClient): BlobContainerClient {
        return blobServiceClient.getBlobContainerClient(containerName)
    }
}

