package com.vengateshm.plugins

import com.vengateshm.routing.authenticationRoutes
import com.vengateshm.routing.notesRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Welcome to Note It App!")
        }
    }

    notesRoutes()
    authenticationRoutes()
}
