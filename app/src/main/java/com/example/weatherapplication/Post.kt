package com.example.weatherapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDate
import java.util.*

open class Post (
    @PrimaryKey
    var id: Int? = null,
    //var date: LocalDate? = null,
    var text: String? = null
) : RealmObject()