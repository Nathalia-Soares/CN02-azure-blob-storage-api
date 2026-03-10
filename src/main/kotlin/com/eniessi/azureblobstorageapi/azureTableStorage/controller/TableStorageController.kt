package com.eniessi.azureblobstorageapi.azureTableStorage.controller

import com.eniessi.azureblobstorageapi.azureTableStorage.model.ImageUploadLog
import com.eniessi.azureblobstorageapi.azureTableStorage.service.AzureTableStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Azure Table Storage", description = "Operações de gerenciamento de logs de upload no Azure Table Storage")
class TableStorageController(
    private val azureTableStorageService: AzureTableStorageService
) {

    @GetMapping("/list")
    @Operation(summary = "Listar todos os logs", description = "Lista todos os logs de upload de imagens")
    fun listAllLogs(): ResponseEntity<List<ImageUploadLog>> {
        return try {
            val logs = azureTableStorageService.listAllLogs()
            ResponseEntity.ok(logs)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/file/{fileName}")
    @Operation(summary = "Buscar log por nome do arquivo", description = "Busca o log de upload de um arquivo específico")
    fun getLogByFileName(@PathVariable fileName: String): ResponseEntity<ImageUploadLog> {
        return try {
            val log = azureTableStorageService.getLogByFileName(fileName)
            if (log != null) {
                ResponseEntity.ok(log)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Buscar logs por data", description = "Busca logs de upload filtrados por data (formato: dd/MM/yyyy)")
    fun getLogsByDate(@PathVariable date: String): ResponseEntity<List<ImageUploadLog>> {
        return try {
            val logs = azureTableStorageService.getLogsByDate(date)
            ResponseEntity.ok(logs)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/ip/{ip}")
    @Operation(summary = "Buscar logs por IP", description = "Busca logs de upload filtrados por endereço IP")
    fun getLogsByIp(@PathVariable ip: String): ResponseEntity<List<ImageUploadLog>> {
        return try {
            val logs = azureTableStorageService.getLogsByIp(ip)
            ResponseEntity.ok(logs)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @Operation(summary = "Deletar log", description = "Deleta o log de upload de um arquivo específico")
    fun deleteLog(@PathVariable fileName: String): ResponseEntity<Map<String, String>> {
        return try {
            val success = azureTableStorageService.deleteLog(fileName)
            if (success) {
                ResponseEntity.ok(mapOf("message" to "Log do arquivo '$fileName' deletado com sucesso"))
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("error" to "Log não encontrado"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Erro ao deletar log: ${e.message}"))
        }
    }
}