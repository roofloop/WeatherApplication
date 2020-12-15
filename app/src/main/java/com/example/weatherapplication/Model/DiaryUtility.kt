package com.example.weatherapplication.Model

object DiaryUtility {
    /*
     *input invalid if...
     *...diaryText is empty
     */
    fun validateDiaryInput(
            diaryText: String
    ): Boolean {
        if(diaryText.isEmpty()) {
            return false
        }
        return true
    }
}