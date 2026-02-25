package com.eniessi.azureblobstorageapi.model

data class UploadResponse(
    val fileName: String,
    val url: String,
    val message: String
)

