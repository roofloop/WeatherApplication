package com.example.weatherapplication.Interface

import com.example.weatherapplication.Model.Post
import io.realm.Realm

class PostModel : PostInterface {

    override fun addPost(realm: Realm, post: Post): Boolean {

        return try {
            realm.beginTransaction()

            val currentIdNum: Number? = realm.where(Post::class.java).max("id")

            val nextId: Int

            nextId = if (currentIdNum == null) {
                1
            } else {
                currentIdNum.toInt() + 1
            }

            post.id = nextId

            realm.copyToRealmOrUpdate(post)

            realm.commitTransaction()

            true
        } catch (e: Exception) {
            println(e)
            false
        }

    }

    override fun deletePost(realm: Realm, int: Int): Boolean {

        try {
            realm.beginTransaction()
            realm.where(Post :: class.java).equalTo("id",int).findFirst()?.deleteFromRealm()
            realm.commitTransaction()
            return true
        } catch (e: Exception) {
            println(e)
            return false
        }
    }

    override fun updatePost(realm: Realm, post: Post): Boolean {
        TODO("Not yet implemented")
    }
}