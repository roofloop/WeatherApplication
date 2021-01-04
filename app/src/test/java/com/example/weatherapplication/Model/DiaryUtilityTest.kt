package com.example.weatherapplication.Model

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class DiaryUtilityTest {
    @Test
    fun `empty diary field returns false`() {
        val result = DiaryUtility.validateDiaryInput(
                ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `nonempty diary field returns true`() {
        val result = DiaryUtility.validateDiaryInput(
                "Hej"
        )
        assertThat(result).isTrue()
    }
}