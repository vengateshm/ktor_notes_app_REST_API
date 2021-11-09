package com.vengateshm.routing

import com.vengateshm.db.DatabaseConnection
import com.vengateshm.entities.Notes
import com.vengateshm.models.Note
import com.vengateshm.models.NoteRequest
import com.vengateshm.models.NoteResponse
import com.vengateshm.models.toNote
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes() {
    val db = DatabaseConnection.database

    routing {
        get("/notes") {
            val notes = db.from(Notes)
                .select()
                .map { it.toNote() }
            call.respond(notes)
        }

        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(Notes) {
                set(it.note, request.note)
            }

            if (result == 1) {
                // Send successfully response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "Values has been successfully inserted"
                    )
                )
            } else {
                // Send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Failed to insert values."
                    )
                )
            }
        }

        get("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val note = db.from(Notes)
                .select()
                .where { Notes.id eq id }
                .map { it.toNote() }.firstOrNull()

            if (note == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    NoteResponse(
                        success = false,
                        data = "Could not found note with  id = $id"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = note
                    )
                )
            }
        }

        put("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val updatedNote = call.receive<NoteRequest>()

            val rowsEffected = db.update(Notes) {
                set(it.note, updatedNote.note)
                where {
                    it.id eq id
                }
            }

            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = "Note has been updated"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Note failed to update"
                    )
                )
            }
        }

        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val rowsEffected = db.delete(Notes) {
                it.id eq id
            }

            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = "Note has been delete"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Note failed to delete"
                    )
                )
            }
        }
    }
}