package com.vengateshm.models

import com.vengateshm.entities.Users
import org.ktorm.dsl.QueryRowSet

data class User(
    val id: Int,
    val username: String,
    val password: String
)

fun QueryRowSet.toUser() = User(
    id = this[Users.id] ?: -1,
    username = this[Users.username] ?: "",
    password = this[Users.password] ?: ""
)