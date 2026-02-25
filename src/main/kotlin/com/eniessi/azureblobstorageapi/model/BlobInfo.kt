package com.eniessi.azureblobstorageapi.model

import java.time.OffsetDateTime

data class BlobInfo(
    val name: String,
    val size: Long,
    val contentType: String?,
    val lastModified: OffsetDateTime?,
    val url: String
)

