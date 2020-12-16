package com.example.weatherapplication.Model

import java.io.Serializable

class PostFirestore: Serializable {

    var id: String? = null
    var diaryInput: String? = null
    var temp: String? = null

    constructor() {}

    constructor(id: String, diaryInput: String, temp: String) {
        this.id = id
        this.diaryInput = diaryInput
        this.temp = temp
    }


}