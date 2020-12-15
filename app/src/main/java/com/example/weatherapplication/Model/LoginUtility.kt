package com.example.weatherapplication.Model

object LoginUtility {
    /*
     * input invalid if...
     * ...username/password is empty
     */
    fun validateLoginInput(
            email: String,
            password: String
    ): Boolean {
        if(email.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }
}