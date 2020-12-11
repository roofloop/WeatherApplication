package com.example.weatherapplication.Model

class PostFirestore {

    var id: String? = null
    var diaryInput: String? = null
    var temp: String? = null

    constructor() {}

    constructor(id: String, diaryInput: String, temp: String) {
        this.id = id
        this.diaryInput = diaryInput
        this.temp = temp
    }

    constructor(diaryInput: String, temp: String) {
        this.diaryInput = diaryInput
        this.temp = temp
    }
}