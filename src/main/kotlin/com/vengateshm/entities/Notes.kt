package com.vengateshm.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Notes: Table<Nothing>("note") {
    val id = int("id").primaryKey()
    val note = varchar("note")
}