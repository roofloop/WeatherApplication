package com.example.weatherapplication.Model

object EncryptionBin {

    fun encrypt(postToEncrypt: String, key: String): String {
        println("Original Post: $postToEncrypt")
        val encryptedPost: StringBuilder = StringBuilder()
        var counter = 0

        for (ch in postToEncrypt) {
            if (counter >= key.length) {
                counter = 0
            }
            val convertPostChToBinary = convertToBinary(ch, 8)
            val convertKeyChToBinary = convertToBinary(key[counter], 8)

            encryptedPost.append(exclusiveOr(convertPostChToBinary, convertKeyChToBinary, false))

            counter += 1
        }
        println("Encrypted Post: $encryptedPost")
        return encryptedPost.toString()
    }

    fun decrypt(postToDecrypt: String, key: String): String {
        if (!isBinary(postToDecrypt)) {
            return "File Error: This file is corrupt or incomplete"
        }
        var i = 0
        var j = 0
        val chars = CharArray(postToDecrypt.length / 8)
        while (i < postToDecrypt.length) {
            if (j >= key.length) {
                j = 0
            }
            val p = postToDecrypt.substring(i, i + 8)
            val ck = convertToBinary(key[j], 8)
            val subStr = exclusiveOr(postToDecrypt.substring(i, i + 8), convertToBinary(key[j], 8), true)
            val nb = Integer.parseInt(subStr, 2)
            chars[i / 8] = nb.toChar()
            i += 8
            j += 1
        }
        val print = String(chars)
        println("Decrypted: $print")
        return String(chars)
    }

    private fun exclusiveOr(bit: String, key: String, reverse: Boolean): String {
        val builder: StringBuilder = StringBuilder()
        var counter = 0
        for (byte in bit) {
            if (!reverse) {
                if (byte.toInt() == key[counter].toInt()) {
                    builder.append("0")
                } else {
                    builder.append("1")
                }
            } else {
                if (byte.toInt() == 49) {
                    if (key[counter].toInt() == 49) {
                        builder.append("0")
                    } else {
                        builder.append("1")
                    }
                } else if (byte.toInt() == 48) {
                    builder.append(key[counter])
                }
                val k = key[counter]
            }
            counter += 1
        }
        return builder.toString()
    }

    private fun convertToBinary(ch: Char, length: Int): String {
        val ascii = ch.toInt()
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