package com.example.weatherapplication.Model

object EncryptionBin {

    fun encrypt(postToEncrypt: String, key: String): String {
        var encryptedPost: StringBuilder = StringBuilder()
        var counter = 0

        for (ch in postToEncrypt) {
            if (counter >= key.length) {
                counter = 0
            }
            val convertPostChToAscii = ch.toInt()
            println("convertPostChToAscii: $convertPostChToAscii")
            val convertKeyChToAscii = key[counter].toInt()
            val convertPostChToBinary = convertToBinary(convertPostChToAscii, 8)
            val convertKeyChToBinary = convertToBinary(convertKeyChToAscii, 8)
            println("convertPostChToBinary: $convertPostChToBinary")
            println("convertKeyChToBinary: $convertKeyChToBinary")
            encryptedPost.append(exclusiveOr(convertPostChToBinary, convertKeyChToBinary))

            counter += 1

        }
        println(encryptedPost)
        return encryptedPost.toString()
    }

    fun decrypt(postToDecrypt: String, key: String) {

    }

    private fun exclusiveOr(bit: String, key: String): String {
        val builder: StringBuilder = StringBuilder()
        var counter = 0
        for (byte in bit) {
            if (byte.toInt() == key[counter].toInt()) {
                builder.append("0")
            } else {
                builder.append("1")
            }
            counter += 1
        }
        println("builder: $builder")
        return builder.toString()
    }

    fun convertToBinary(ascii: Int, length: Int): String {
        return String.format(
                "%" + length + "s",
                Integer.toBinaryString(ascii)
        ).replace(" ".toRegex(), "0")
    }

    fun convertFromBinary(binary: String): String {
        if (!isBinary(binary)) {
            return "Not a binary value"
        }
        val chars = CharArray(binary.length / 8)
        var i = 0
        while (i < binary.length) {
            val str = binary.substring(i, i + 8)
            val nb = Integer.parseInt(str, 2)
            chars[i / 8] = nb.toChar()
            i += 8
        }
        return String(chars)
    }

    private fun isBinary(text: String?): Boolean {
        if (text != null && text.length % 8 == 0) {
            for (char in text.toCharArray()) {
                if (char != '0' && char != '1') {
                    return false
                }
            }
            return true
        }
        return false
    }

}