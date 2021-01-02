package com.example.weatherapplication.Model

object EncryptionBin {

    fun encrypt(postToEncrypt: String, key: String): String {
        println("Original Post: $postToEncrypt")
        val encryptedPost: StringBuilder = StringBuilder()
        var counter = 0

        // move thrue each character in the message
        for (ch in postToEncrypt) {
            // reset counter if it surpasses key length
            if (counter >= key.length) {
                counter = 0
            }
            // convert the character to binary
            val convertPostChToBinary = convertToBinary(ch, 8)
            // convert the matching key-value at [counter] to binary
            val convertKeyChToBinary = convertToBinary(key[counter], 8)

            // append the character after going thrue the exclusiveOr function (encrypted)
            encryptedPost.append(exclusiveOr(convertPostChToBinary, convertKeyChToBinary, false))

            counter += 1

        }
        println("Encrypted Post: $encryptedPost")
        // return the fully encrypted message
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
            // go thrue key digits, start from 0 when done
            if (j >= key.length) {
                j = 0
            }
            val p = postToDecrypt.substring(i, i + 8)
            val ck = convertToBinary(key[j], 8)
            // extract the 8-bit length binary char into a substring
            val subStr = exclusiveOr(postToDecrypt.substring(i, i + 8), convertToBinary(key[j], 8), true)
            // parse the sub string to binary (radix 2)
            val numeral = Integer.parseInt(subStr, 2)
            chars[i / 8] = numeral.toChar()
            i += 8
            j += 1
        }
        val print = String(chars)
        println("Decrypted: $print")
        return String(chars)
    }

    // exclusiveOr function. send in the 8-bit binary string, with the 8-bit binary string for the key...
    // ...send in boolean for reverse operation
    // ...if the bits is equal (0 or 1) encrypt to a 0
    //...else encrypt to a 1
    private fun exclusiveOr(bit: String, key: String, reverse: Boolean): String {
        val builder: StringBuilder = StringBuilder()
        var counter = 0
        // extract each binary strings digits one by one
        for (byte in bit) {
            if (!reverse) {
                // compare; if equal encrypt to 0...
                if (byte.toInt() == key[counter].toInt()) {
                    builder.append("0")
                    //...else encrypt to 1
                } else {
                    builder.append("1")
                }
            } else {
                // reverse. if bit is equal to 1 (ascii 49), check key and apply the opposite...
                    //...else apply 1
                if (byte.toInt() == 49) {
                    if (key[counter].toInt() == 49) {
                        builder.append("0")
                    } else {
                        builder.append("1")
                    }
                // if bit is equal to 0 (ascii 48), decrypt to key bit
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
        // convert character to ascii
        val ascii = ch.toInt()
        // return converted ascii to binary, fill upp with 0's if binary string is below 8 bits
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
        // check if string is not null, evenly divided by 8 (8 bits), and contains 1's and 0's.
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