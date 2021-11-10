package com.vengateshm.utils

import com.vengateshm.entities.Users
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Database.findUserByName(userName: String) =
    this.from(Users)
        .select()
        .where { Users.username eq userName }
        .map { it[Users.username] }
        .firstOrNull()
