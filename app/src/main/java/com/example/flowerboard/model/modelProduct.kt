package com.example.flowerboard.model

class modelProduct {
    //Define the model variable of Product
    var uid: String =""
    var id: String =""
    var title: String =""
    var description: String =""
    var categoryId: String =""
    var url: String =""
    var timestamp: Long = 0
    var price: String =""

    constructor()

    constructor(uid: String, id: String, productTitle: String, productDescription: String, categoryId: String, url: String, timestamp: Long, productPrice: String) {
        this.uid = uid
        this.id = id
        this.title = productTitle
        this.description = productDescription
        this.categoryId = categoryId
        this.url = url
        this.timestamp = timestamp
        this.price = productPrice
    }
}