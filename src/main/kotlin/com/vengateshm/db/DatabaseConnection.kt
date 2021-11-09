package com.vengateshm.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:postgresql://ec2-3-220-90-40.compute-1.amazonaws.com:5432/d8qpvq9u0kej1h",
        driver = "org.postgresql.Driver",
        user = "wbgvkrnfjdnpkc",
        password = "07fb1b95f8f3d2e7446c0db1cd58d759f45723f61ba023081f482fc2f0473bbe"
    )
}