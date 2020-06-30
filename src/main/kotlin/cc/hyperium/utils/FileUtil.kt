package cc.hyperium.utils

import java.io.File
import java.io.OutputStream
import java.security.MessageDigest

/**
 * Gets the SHA-256 hash of a file.
 */
val File.sha256
    get() = inputStream().use {
        MessageDigest.getInstance("SHA-256").apply {
            it.copyTo(object : OutputStream() {
                override fun write(b: Int) {
                    update(b.toByte())
                }

                override fun write(b: ByteArray, off: Int, len: Int) {
                    update(b, off, len)
                }
            }
            )
        }.digest().joinToString(separator = "") { String.format("%02x", it) }
    }