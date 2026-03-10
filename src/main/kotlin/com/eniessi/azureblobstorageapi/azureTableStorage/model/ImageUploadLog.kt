package com.eniessi.azureblobstorageapi.azureTableStorage.model

import com.azure.data.tables.models.TableEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ImageUploadLog(
    val nomeDaFoto: String,
    val dataDeCadastro: String,
    val horarioDoCadastro: String,
    val enderecoIp: String
) {
    companion object {
        fun toTableEntity(log: ImageUploadLog, partitionKey: String = "ImageUploads"): TableEntity {
            val entity = TableEntity(partitionKey, log.nomeDaFoto)
            entity.addProperty("nomeDaFoto", log.nomeDaFoto)
            entity.addProperty("dataDeCadastro", log.dataDeCadastro)
            entity.addProperty("horarioDoCadastro", log.horarioDoCadastro)
            entity.addProperty("enderecoIp", log.enderecoIp)
            return entity
        }

        fun fromTableEntity(entity: TableEntity): ImageUploadLog {
            return ImageUploadLog(
                nomeDaFoto = entity.getProperty("nomeDaFoto") as String,
                dataDeCadastro = entity.getProperty("dataDeCadastro") as String,
                horarioDoCadastro = entity.getProperty("horarioDoCadastro") as String,
                enderecoIp = entity.getProperty("enderecoIp") as String
            )
        }

        fun create(nomeDaFoto: String, enderecoIp: String): ImageUploadLog {
            val dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val horarioAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            return ImageUploadLog(
                nomeDaFoto = nomeDaFoto,
                dataDeCadastro = dataAtual,
                horarioDoCadastro = horarioAtual,
                enderecoIp = enderecoIp
            )
        }
    }
}