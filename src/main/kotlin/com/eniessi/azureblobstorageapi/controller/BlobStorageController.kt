package com.eniessi.azureblobstorageapi.controller

import com.eniessi.azureblobstorageapi.model.BlobInfo
import com.eniessi.azureblobstorageapi.model.UploadResponse
import com.eniessi.azureblobstorageapi.service.AzureBlobStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/blobs")
@Tag(name = "Azure Blob Storage", description = "Operações de gerenciamento de arquivos no Azure Blob Storage")
class BlobStorageController(
    private val azureBlobStorageService: AzureBlobStorageService
) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Upload de arquivo", description = "Faz upload de uma imagem para o Azure Blob Storage")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<UploadResponse> {
        return try {
            if (file.isEmpty) {
                return ResponseEntity.badRequest()
                    .body(UploadResponse("", "", "Arquivo está vazio"))
            }

            val url = azureBlobStorageService.uploadFile(file)
            val response = UploadResponse(
                fileName = file.originalFilename ?: "unknown",
                url = url,
                message = "Upload realizado com sucesso"
            )

            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UploadResponse("", "", "Erro ao fazer upload: ${e.message}"))
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar arquivos", description = "Lista todos os arquivos no container do Azure Blob Storage")
    fun listBlobs(): ResponseEntity<List<BlobInfo>> {
        return try {
            val blobs = azureBlobStorageService.listBlobs()
            ResponseEntity.ok(blobs)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "Download de arquivo", description = "Faz download de um arquivo específico do Azure Blob Storage")
    fun downloadFile(@PathVariable fileName: String): ResponseEntity<ByteArray> {
        return try {
            val fileContent = azureBlobStorageService.downloadFile(fileName)
            val contentType = azureBlobStorageService.getFileContentType(fileName)

            val headers = HttpHeaders()
            headers.contentType = MediaType.parseMediaType(contentType)
            headers.setContentDispositionFormData("attachment", fileName)

            ResponseEntity.ok()
                .headers(headers)
                .body(fileContent)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/view/{fileName}")
    @Operation(summary = "Visualizar arquivo", description = "Visualiza um arquivo diretamente no navegador")
    fun viewFile(@PathVariable fileName: String): ResponseEntity<ByteArray> {
        return try {
            val fileContent = azureBlobStorageService.downloadFile(fileName)
            val contentType = azureBlobStorageService.getFileContentType(fileName)

            val headers = HttpHeaders()
            headers.contentType = MediaType.parseMediaType(contentType)
            headers.setContentDispositionFormData("inline", fileName)

            ResponseEntity.ok()
                .headers(headers)
                .body(fileContent)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @Operation(summary = "Deletar arquivo", description = "Deleta um arquivo do Azure Blob Storage")
    fun deleteFile(@PathVariable fileName: String): ResponseEntity<Map<String, String>> {
        return try {
            azureBlobStorageService.deleteFile(fileName)
            ResponseEntity.ok(mapOf("message" to "Arquivo '$fileName' deletado com sucesso"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to e.message!!))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Erro ao deletar arquivo: ${e.message}"))
        }
    }
}

