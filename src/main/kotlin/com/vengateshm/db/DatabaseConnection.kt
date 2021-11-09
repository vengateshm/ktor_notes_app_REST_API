package com.vengateshm.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:postgresql://ec2-34-203-182-172.compute-1.amazonaws.com:5432/dfmchkkfocl56e",
        driver = "org.postgresql.Driver",
        user = "veepddedhnjvcu",
        password = "6f7f829f13eeda204e7134645014a640755c58cbf65a6539acfb87c29163558d"
    )
}