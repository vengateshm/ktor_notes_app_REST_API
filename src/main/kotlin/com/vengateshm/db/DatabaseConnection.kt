package com.vengateshm.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = System.getenv("DB_URL"),
        driver = System.getenv("DB_DRIVER"),
        user = System.getenv("DB_USERNAME"),
        password = System.getenv("DB_PASSWORD")
    )
}