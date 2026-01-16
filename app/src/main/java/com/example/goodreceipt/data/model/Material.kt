package com.example.goodreceipt.data.model

data class Material(
    val id: String,
    val name: String,
    val sku: String,
    val location: String,
    val warehouse: String,
    val sector: String,
    val unit: String,
    val quantity: Int,
    val poNumber: String,
    val vendorNumber: String,
    val deliveryDate: String,
    val iconRes: Int = 0 // Drawable resource ID for icon
) {
    val fullLocation: String
        get() = "$warehouse - $sector"
}
