package com.example.weatherapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Post (
    @PrimaryKey
    var id: Int? = null,
    var date: Date? = null,
    var text: String? = null
) : RealmObject()