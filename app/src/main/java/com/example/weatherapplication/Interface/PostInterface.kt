package com.example.weatherapplication.Interface

import com.example.weatherapplication.Model.Post
import io.realm.Realm

interface PostInterface {

    fun addPost(realm: Realm, post: Post):Boolean
    fun deletePost(realm: Realm, id: Int):Boolean
    fun updatePost(realm: Realm, post: Post):Boolean

}