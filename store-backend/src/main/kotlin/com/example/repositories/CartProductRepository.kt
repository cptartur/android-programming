package com.example.repositories

import com.example.models.Product
import com.example.tables.CartsProducts
import com.example.tables.Products
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CartProductRepository {
    fun createOrUpdate(id: Int, product: Product, amount: Int): Int {
        transaction {
            CartsProducts.update({ CartsProducts.productId eq product.id }) {
                it[CartsProducts.amount] = amount
            }
            CartsProducts.insertIgnore {
                it[cartId] = id
                it[productId] = product.id
                it[CartsProducts.amount] = amount
            }
        }
        return id
    }

    fun updateAmount(cartId: Int, product: Product, amount: Int): Boolean {
        val status = transaction {
            CartsProducts.update({ CartsProducts.cartId eq cartId }) {
                it[CartsProducts.amount] = amount
            }
        }
        return status == 1
    }

    fun remove(cartId: Int, productId: Int): Boolean {
        val status = transaction {
            CartsProducts.deleteWhere { (CartsProducts.productId eq productId) and (CartsProducts.cartId eq cartId) }
        }
        return status == 1
    }

    fun removeAll(cartId: Int): Boolean {
        val status = transaction {
            CartsProducts.deleteWhere { CartsProducts.cartId eq cartId }
        }
        return status == 1
    }

    fun findById(cartId: Int): List<ProductWithAmount> {
        return transaction {
            CartsProducts.join(
                Products,
                JoinType.INNER,
                additionalConstraint = { CartsProducts.productId eq Products.id })
                .slice(
                    Products.id,
                    Products.price,
                    Products.name,
                    Products.description,
                    Products.categoryId,
                    Products.imageUrl,
                    CartsProducts.amount
                )
                .select { CartsProducts.cartId eq cartId }
                .map {
                    ProductWithAmount(
                        Product(
                            it[Products.name],
                            it[Products.description],
                            it[Products.price],
                            it[Products.id].value,
                            it[Products.categoryId],
                            it[Products.imageUrl]
                        ), it[CartsProducts.amount]
                    )
                }
        }
    }
}

data class ProductWithAmount(val product: Product, val amount: Int)