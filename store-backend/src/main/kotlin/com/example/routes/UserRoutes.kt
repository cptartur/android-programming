package com.example.routes

import com.example.models.User
import com.example.repositories.UserRepository
import com.example.tables.Users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureUserRoutes() {
    routing {
        get("/user/{id}") {
            val id = call.parameters["id"]?.toInt()
            val user = id?.let { it1 -> UserRepository.findById(it1) }
            user?.let { call.respond(it) }
        }

        post("/user") {
            val user = call.receive(User::class)
            val tableID = UserRepository.create(user)
            call.response.apply {
                status(HttpStatusCode.Created)
                headers.append(HttpHeaders.Location, tableID.toString())
            }
        }

        put("/user/{id}") {
            val id = call.parameters["id"]?.toInt()
            val user = call.receive(User::class)
            user.let {
                if (id != null) {
                    UserRepository.update(id, user)
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }

        delete("/user/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val succeeded = UserRepository.remove(id)
                if (succeeded) {
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }
    }
}