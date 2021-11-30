package com.example.routes

import com.example.repositories.ProductRepository
import com.example.repositories.Repository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.reflect.KClass


inline fun <reified TModel: Any> Application.configureCRUDRoutes(
    routeString: String,
    repository: Repository<TModel>
) {
    routing {
        get("/$routeString") {
            call.respond(repository.findAll())
        }

        get("/$routeString/{id}") {
            val id = call.parameters["id"]?.toInt()
            val obj = id?.let { it -> repository.findById(it) }
            obj?.let { call.respond(it) }
        }

        post("/$routeString/{id}") {
            val product = call.receive(TModel::class)
            val tableID = repository.create(product)
            call.response.apply {
                status(HttpStatusCode.Created)
                headers.append(HttpHeaders.Location, tableID.toString())
            }
        }

        put("/$routeString/{id}") {
            val id = call.parameters["id"]?.toInt()
            val obj = call.receive(TModel::class)
            obj.let {
                if (id != null) {
                    repository.update(id, obj)
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }

        delete("/$routeString/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val succeeded = repository.remove(id)
                if (succeeded) {
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }
    }
}