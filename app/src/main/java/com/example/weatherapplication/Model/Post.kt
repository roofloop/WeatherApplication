package com.example.weatherapplication.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Post (
    @PrimaryKey
    var id: Int? = null,
    var text: String? = null,
    var temp: String? = null
) : RealmObject()