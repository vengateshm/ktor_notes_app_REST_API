package com.vengateshm.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse<out T>(
    val data: T,
    val success: Boolean
)
