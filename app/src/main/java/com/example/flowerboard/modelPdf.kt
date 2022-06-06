package com.example.flowerboard

class modelPdf {

    //variables
    var uid: String =""
    var id: String =""
    var title: String =""
    var description: String =""
    var categoryId: String =""
    var url: String =""
    var timestamp: Long = 0
    var price: String =""

    //empty constructor (required by firebase)
    constructor()

    //parameterized constructor
    constructor(
        uid: String,
        id: String,
        title: String,
        description: String,
        categoryId: String,
        url: String,
        timestamp: Long,
        price: String
    ) {
        this.uid = uid
        this.id = id
        this.title = title
        this.description = description
        this.categoryId = categoryId
        this.url = url
        this.timestamp = timestamp
        this.price = price
    }



}