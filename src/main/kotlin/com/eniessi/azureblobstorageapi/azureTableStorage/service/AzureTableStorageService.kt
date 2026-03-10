package com.eniessi.azureblobstorageapi.azureTableStorage.service

import com.azure.data.tables.TableClient
import com.azure.data.tables.models.ListEntitiesOptions
import com.eniessi.azureblobstorageapi.azureTableStorage.model.ImageUploadLog
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AzureTableStorageService(
    @Autowired(required = false) private val tableClient: TableClient?
) {

    private val logger = LoggerFactory.getLogger(AzureTableStorageService::class.java)

    fun logImageUpload(nomeDaFoto: String, enderecoIp: String): ImageUploadLog {
        val log = ImageUploadLog.create(nomeDaFoto, enderecoIp)

        if (tableClient == null) {
            logger.warn("TableClient is not available. Log will not be persisted: $nomeDaFoto")
            return log
        }

        val entity = ImageUploadLog.toTableEntity(log)

        try {
            tableClient.createEntity(entity)
        } catch (e: Exception) {
            try {
                tableClient.updateEntity(entity)
            } catch (ex: Exception) {
                logger.error("Failed to log upload for $nomeDaFoto: ${ex.message}")
            }
        }

        return log
    }

    fun listAllLogs(): List<ImageUploadLog> {
        if (tableClient == null) {
            logger.warn("TableClient is not available. Returning empty list.")
            return emptyList()
        }

        val logs = mutableListOf<ImageUploadLog>()

        try {
            tableClient.listEntities().forEach { entity ->
                try {
                    logs.add(ImageUploadLog.fromTableEntity(entity))
                } catch (e: Exception) { }
            }
        } catch (e: Exception) {
            logger.error("Failed to list logs: ${e.message}")
        }

        return logs
    }

    fun getLogByFileName(fileName: String): ImageUploadLog? {
        if (tableClient == null) {
            logger.warn("TableClient is not available.")
            return null
        }

        return try {
            val entity = tableClient.getEntity("ImageUploads", fileName)
            ImageUploadLog.fromTableEntity(entity)
        } catch (e: Exception) {
            logger.error("Failed to get log for $fileName: ${e.message}")
            null
        }
    }

    fun getLogsByDate(date: String): List<ImageUploadLog> {
        if (tableClient == null) {
            logger.warn("TableClient is not available. Returning empty list.")
            return emptyList()
        }

        val filter = "dataDeCadastro eq '$date'"
        val options = ListEntitiesOptions().setFilter(filter)

        val logs = mutableListOf<ImageUploadLog>()
        try {
            tableClient.listEntities(options, null, null).forEach { entity ->
                try {
                    logs.add(ImageUploadLog.fromTableEntity(entity))
                } catch (e: Exception) { }
            }
        } catch (e: Exception) {
            logger.error("Failed to list logs by date: ${e.message}")
        }

        return logs
    }

    fun getLogsByIp(ip: String): List<ImageUploadLog> {
        if (tableClient == null) {
            logger.warn("TableClient is not available. Returning empty list.")
            return emptyList()
        }

        val filter = "enderecoIp eq '$ip'"
        val options = ListEntitiesOptions().setFilter(filter)

        val logs = mutableListOf<ImageUploadLog>()
        try {
            tableClient.listEntities(options, null, null).forEach { entity ->
                try {
                    logs.add(ImageUploadLog.fromTableEntity(entity))
                } catch (e: Exception) { }
            }
        } catch (e: Exception) {
            logger.error("Failed to list logs by IP: ${e.message}")
        }

        return logs
    }

    fun deleteLog(fileName: String): Boolean {
        if (tableClient == null) {
            logger.warn("TableClient is not available.")
            return false
        }

        return try {
            tableClient.deleteEntity("ImageUploads", fileName)
            true
        } catch (e: Exception) {
            logger.error("Failed to delete log for $fileName: ${e.message}")
            false
        }
    }

    fun isAvailable(): Boolean = tableClient != null
}



