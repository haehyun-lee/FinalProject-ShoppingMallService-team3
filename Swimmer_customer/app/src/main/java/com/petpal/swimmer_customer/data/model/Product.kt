package com.petpal.swimmer_customer.data.model

import com.google.firebase.database.PropertyName

var productList = mutableListOf<Product>()

data class Product(
    val category: Category,
    val code: String,
    val description: String,
    val descriptionImage: String,
    val hashTag: List<String>,
    val mainImage: List<String>,
    val name: String,
    val orderCount: Int,
    val price: Int,
    val productUid: String,
    val regDate: String,
    val sellerUid: String,
    val infoImage: String
)

data class Category(
    val main: String,
    val mid: String,
    val sub: String
)