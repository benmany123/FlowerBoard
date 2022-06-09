package com.example.flowerboard.model

class modelCategory {
    //Define the model variable of Category
    var id:String = ""
    var category:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    constructor()

    constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }
}