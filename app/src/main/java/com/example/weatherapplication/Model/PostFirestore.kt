package com.example.weatherapplication.Model

import java.io.Serializable

class PostFirestore: Serializable {

    var id: String? = null
    var diaryInput: String? = null
    var temp: String? = null
    var creationDate: String? = null

    constructor() {}

    constructor(id: String, diaryInput: String, temp: String, creationDate: String) {
        this.id = id
        this.diaryInput = diaryInput
        this.temp = temp
        this.creationDate = creationDate

    }

}