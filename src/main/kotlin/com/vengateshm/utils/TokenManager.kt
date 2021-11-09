package com.vengateshm.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.vengateshm.models.User
import io.ktor.config.*
import java.util.*

class TokenManager(private val config: HoconApplicationConfig) {
    private val audience = config.property("audience").getString()
    private val secret = config.property("secret").getString()
    private val issuer = config.property("issuer").getString()
    private val expirationDate = System.currentTimeMillis() + 600000;

    fun generateJWTToken(user: User): String {

        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
        return token
    }

    fun verifyJWTToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}