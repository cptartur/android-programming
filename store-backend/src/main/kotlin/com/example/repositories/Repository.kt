package com.example.repositories

interface Repository<T> {
    fun create(obj: T): Int
    fun update(id: Int, obj: T): Boolean
    fun remove(id: Int): Boolean
    fun findById(id: Int): T?
    fun findAll(): List<T>
}