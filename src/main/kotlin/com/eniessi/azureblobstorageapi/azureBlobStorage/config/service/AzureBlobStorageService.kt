package com.eniessi.azureblobstorageapi.azureBlobStorage.config.service

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.models.BlobHttpHeaders
import com.eniessi.azureblobstorageapi.azureBlobStorage.config.model.BlobInfo
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

@Service
class AzureBlobStorageService(
    private val blobContainerClient: BlobContainerClient
) {

    fun uploadFile(file: MultipartFile): String {
        val blobName = file.originalFilename ?: throw IllegalArgumentException("Arquivo sem nome")
        val blobClient = blobContainerClient.getBlobClient(blobName)

        val headers = BlobHttpHeaders()
            .setContentType(file.contentType)

        blobClient.upload(file.inputStream, file.size, true)
        blobClient.setHttpHeaders(headers)

        return blobClient.blobUrl
    }

    fun listBlobs(): List<BlobInfo> {
        val blobs = mutableListOf<BlobInfo>()

        blobContainerClient.listBlobs().forEach { blobItem ->
            val blobClient = blobContainerClient.getBlobClient(blobItem.name)
            val properties = blobClient.properties

            blobs.add(
                BlobInfo(
                    name = blobItem.name,
                    size = properties.blobSize,
                    contentType = properties.contentType,
                    lastModified = properties.lastModified,
                    url = blobClient.blobUrl
                )
            )
        }

        return blobs
    }

    fun downloadFile(fileName: String): ByteArray {
        val blobClient = blobContainerClient.getBlobClient(fileName)

        if (!blobClient.exists()) {
            throw IllegalArgumentException("Arquivo '$fileName' não encontrado")
        }

        val outputStream = ByteArrayOutputStream()
        blobClient.downloadStream(outputStream)

        return outputStream.toByteArray()
    }

    fun getFileContentType(fileName: String): String {
        val blobClient = blobContainerClient.getBlobClient(fileName)

        if (!blobClient.exists()) {
            throw IllegalArgumentException("Arquivo '$fileName' não encontrado")
        }

        return blobClient.properties.contentType ?: "application/octet-stream"
    }

    fun deleteFile(fileName: String): Boolean {
        val blobClient = blobContainerClient.getBlobClient(fileName)

        if (!blobClient.exists()) {
            throw IllegalArgumentException("Arquivo '$fileName' não encontrado")
        }

        blobClient.delete()
        return true
    }
}

