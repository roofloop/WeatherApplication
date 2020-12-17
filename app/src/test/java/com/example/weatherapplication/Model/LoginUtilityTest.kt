package com.example.weatherapplication.Model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoginUtilityTest {
    @Test
    fun `empty username returns false`() {
        val result = LoginUtility.validateLoginInput(
                "",
                "hej123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and password returns true`() {
        val result = LoginUtility.validateLoginInput(
                "test@test.com",
                "hej123"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `empty password returns false`() {
        val result = LoginUtility.validateLoginInput(
                "test@test.com",
                ""
        )
        assertThat(result).isFalse()
    }
}