package com.vipuljha.ktxnews.utils

import android.util.Base64

object Helper {

    // Function to encode a string to Base64
    fun encodeToBase64(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
    }

    // Function to decode a Base64 string back to normal string
    fun decodeFromBase64(base64: String): String {
        return String(Base64.decode(base64, Base64.DEFAULT))
    }
}