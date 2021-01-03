package com.example.weatherapplication.Model

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class EncryptionBinTest {

    // testing encryption

    @Test
    fun `base testing function`() {
        // testing the actual encryption function
        val result = EncryptionBin.encrypt("Hej! Jag är här", "fckcjhjhcd")
        assertThat(result)
    }

    @Test
    fun `encryption and decryption test`() {
        // testing to encrypt and then decrypt
        val post = "Haj! Ett lite längre, med å ä ö8"
        val key = "foickmeadasdf"
        val encryptedText = EncryptionBin.encrypt(post, key)
        val decryptedText = EncryptionBin.decrypt(encryptedText, key)
        assertThat(decryptedText).isEqualTo(post)
    }

    /*
    @Test
    fun `testing conversions`() {
        val str = "H"
        val strToAscii = str[0]
        println("strTOAscii: $strToAscii")
        val result = EncryptionBin.convertFromBinary(EncryptionBin.convertToBinary(strToAscii, 8))
        println(result)
        assertThat(result).isEqualTo("H")
    }
    */

}