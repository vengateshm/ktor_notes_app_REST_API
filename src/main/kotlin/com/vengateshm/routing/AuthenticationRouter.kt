package com.vengateshm.routing

import com.vengateshm.db.DatabaseConnection
import com.vengateshm.entities.Users
import com.vengateshm.models.NoteResponse
import com.vengateshm.models.User
import com.vengateshm.models.UserCredentials
import com.vengateshm.utils.TokenManager
import com.typesafe.config.ConfigFactory
import com.vengateshm.models.toUser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Username should be greater than or equal to 3 and password should be greater than or equal to 8"
                    )
                )
                return@post
            }

            val username = userCredentials.username.lowercase(Locale.getDefault())
            val password = userCredentials.hashedPassword()

            // Check if username already exists
            val user = db.from(Users)
                .select()
                .where { Users.username eq username }
                .map { it[Users.username] }
                .firstOrNull()

            if (user != null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(success = false, data = "User already exists, please try a different username")
                )
                return@post
            }

            db.insert(Users) {
                set(it.username, username)
                set(it.password, password)
            }

            call.respond(
                HttpStatusCode.Created,
                NoteResponse(success = true, data = "User has been successfully created")
            )
        }

        post("/login") {
            val userCredentials = call.receive<UserCredentials>()

            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Username should be greater than or equal to 3 and password should be greater than or equal to 8"
                    )
                )
                return@post
            }

            val username = userCredentials.username.lowercase()
            val password = userCredentials.password

            // Check if user exists
            val user = db.from(Users)
                .select()
                .where { Users.username eq username }
                .map { it.toUser() }
                .firstOrNull()

            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password"
                    )
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)
            if (doesPasswordMatch.not()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password"
                    )
                )
                return@post
            }

            val token = tokenManager.generateJWTToken(user)
            call.respond(
                HttpStatusCode.OK,
                NoteResponse(
                    success = true,
                    data = token
                )
            )
        }

        authenticate {
            get("/me") {
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val userId = principle.payload.getClaim("userId").asInt()
                call.respondText("Hello, $username with id: $userId")
            }
        }
    }
}