package com.vengateshm

import com.vengateshm.plugins.configureRouting
import com.vengateshm.utils.TokenManager
import com.typesafe.config.ConfigFactory
import com.vengateshm.db.DatabaseConnection
import com.vengateshm.utils.findUserByName
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0") {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val tokenManager = TokenManager(config)
        val database = DatabaseConnection.database

        install(Authentication) {
            jwt {
                verifier(tokenManager.verifyJWTToken())
                realm = config.property("realm").getString()
                validate { jwtCredential ->
                    val userNameFromClaim = jwtCredential.payload.getClaim("username").asString()
                    if (userNameFromClaim.isNotEmpty() && database.findUserByName(userNameFromClaim) != null) {
                        // Check username from claim against users table
                        // Can return JWTPrincipal or a class implements Principal marker interface
                        JWTPrincipal(jwtCredential.payload)
                    } else {
                        null
                    }
                }
            }
        }
        install(ContentNegotiation) {
            json()
        }

        configureRouting()
    }.start(wait = true)
}
