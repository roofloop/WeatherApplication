package com.example.weatherapplication.Model

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class EncryptionBinTest {

    @Test
    fun `base testing function`() {
        val result = EncryptionBin.encrypt("Hej! Jag är här", "fckcjhjhcd")
        assertThat(result)
    }

    @Test
    fun `testing conversions`() {
        val str = "H"
        val strToAscii = str[0].toInt()
        println("strTOAscii: $strToAscii")
        val result = EncryptionBin.convertFromBinary(EncryptionBin.convertToBinary(strToAscii, 8))
        println(result)
        assertThat(result).isEqualTo("H")
    }
}