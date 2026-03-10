package com.eniessi.azureblobstorageapi.azureTableStorage.config

import com.azure.data.tables.TableClient
import com.azure.data.tables.TableServiceClient
import com.azure.data.tables.TableServiceClientBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["azure.table.enabled"], havingValue = "true", matchIfMissing = true)
class AzureTableStorageConfig {

    private val logger = LoggerFactory.getLogger(AzureTableStorageConfig::class.java)

    @Value("\${azure.table.connection-string}")
    private lateinit var connectionString: String

    @Value("\${azure.table.name}")
    private lateinit var tableName: String

    @Bean
    fun tableServiceClient(): TableServiceClient? {
        return try {
            TableServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient()
        } catch (e: Exception) {
            logger.error("Failed to create TableServiceClient: ${e.message}")
            null
        }
    }

    @Bean
    fun tableClient(tableServiceClient: TableServiceClient?): TableClient? {
        if (tableServiceClient == null) {
            logger.warn("TableServiceClient is null, skipping TableClient creation")
            return null
        }

        return try {
            try {
                tableServiceClient.createTableIfNotExists(tableName)
                logger.info("Table '$tableName' is ready (created or already exists)")
            } catch (e: Exception) {
                if (e.message?.contains("TableAlreadyExists") == true || e.message?.contains("409") == true) {
                    logger.info("Table '$tableName' already exists, using existing table")
                } else {
                    throw e
                }
            }

            val client = tableServiceClient.getTableClient(tableName)
            logger.info("TableClient for '$tableName' created successfully")
            client
        } catch (e: Exception) {
            logger.error("Failed to create or access table '$tableName': ${e.message}")
            logger.warn("Table Storage logging will be disabled")
            null
        }
    }
}