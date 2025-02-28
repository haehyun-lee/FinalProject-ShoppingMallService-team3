package com.petpal.swimmer_seller.data.model

// 리뷰
data class Review(
    var reviewUid: String,
    var sellerUid: String,
    var height: Long,
    var weight: Long,
    var rating: Double,
    var date: String,
    var content: String,
    var Image: String,
    var size: Long,
    var color: Long
)