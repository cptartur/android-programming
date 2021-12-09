package com.example.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Categories: IntIdTable() {
    var name = varchar("name", 50)
}