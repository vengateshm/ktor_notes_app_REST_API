package com.vengateshm.models

import com.vengateshm.entities.Notes
import kotlinx.serialization.Serializable
import org.ktorm.dsl.QueryRowSet

@Serializable
data class Note(
    val id: Int,
    val note: String
)

fun QueryRowSet.toNote() = Note(
    this[Notes.id] ?: -1,
    this[Notes.note] ?: ""
)
