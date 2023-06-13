package com.npsdk.jetpack_sdk.base.api

import com.npsdk.module.NPayLibrary
import com.npsdk.module.utils.Constants
import com.npsdk.module.utils.Preference
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.io.pem.PemReader
import java.io.StringReader
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.spec.EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Aes encryption
 */
object EncryptServiceHelper {

    private var randomKeyRaw: String? = null
    private var randomKeyEncrypt: String? = null
    private fun padData(data: ByteArray): ByteArray {
        val blockSize = 16
        val paddingSize = blockSize - (data.size % blockSize)
        val paddedData = ByteArray(data.size + paddingSize)
        System.arraycopy(data, 0, paddedData, 0, data.size)
        for (i in data.size until paddedData.size) {
            paddedData[i] = paddingSize.toByte()
        }
        return paddedData
    }

    fun getRandomkeyEncrypt(): String? {
        try {
            val myRandomkey = getPublicKeySaved() ?: return null
            if (randomKeyEncrypt != null) return randomKeyEncrypt!!
            randomKeyEncrypt = encryptRandomkey(getRandomkeyRaw(), myRandomkey)
            return randomKeyEncrypt!!
        } catch (e: Exception) {
            return null
        }
    }

    fun getRandomkeyRaw(): String {
        if (randomKeyRaw != null) return randomKeyRaw!!
        val date = System.currentTimeMillis().toString()
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(date.toByteArray(Charsets.UTF_8)))
        randomKeyRaw = String.format("%032x", bigInt) // Md5 date
        return randomKeyRaw!!
    }

    private fun getPublicKeySaved(): String? {
        return Preference.getString(
            NPayLibrary.getInstance().activity,
            NPayLibrary.getInstance().sdkConfig.env + Constants.PUBLIC_KEY
        )
    }

    fun encryptKeyAesBase64(data: String, encryptionKey: String): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val secretKeySpec = SecretKeySpec(encryptionKey.toByteArray(), "AES")
        val iv = ByteArray(16) // 16-byte IV

        // Initialize the cipher with encryption mode and key
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))

        // Perform the encryption
        val encryptedBytes = cipher.doFinal(padData(data.toByteArray()))

        // Encode the encrypted key using Base64
        return Base64.toBase64String(encryptedBytes)
    }

    fun decryptAesBase64(encryptedData: String, key: String): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val iv = ByteArray(16) // 16-byte IV

        // Initialize the cipher with decryption mode and key
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))

        // Decode the Base64-encoded encrypted data
        val encryptedBytes = Base64.decode(encryptedData)

        // Perform the decryption
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        // Remove null padding
        var paddingSize = 0
        for (i in decryptedBytes.indices.reversed()) {
            if (decryptedBytes[i].toInt() == 0) {
                paddingSize++
            } else {
                break
            }
        }
        val decryptedText = decryptedBytes.copyOfRange(0, decryptedBytes.size - paddingSize)
        var jsonEncoded = String(decryptedText)
        val positionLast = jsonEncoded.lastIndexOf("}")
        if (positionLast != -1) {
            jsonEncoded = jsonEncoded.substring(0, positionLast + 1)
        }

        return jsonEncoded
    }

    fun encryptRandomkey(data: String, publicKey: String): String {
        val publicKeyRaw = "-----BEGIN PUBLIC KEY-----$publicKey-----END PUBLIC KEY-----"
        val reader = PemReader(StringReader(publicKeyRaw))
        val pemObject = reader.readPemObject()
        val keyBytes: ByteArray = pemObject.content
        val keySpec: EncodedKeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherData: ByteArray = cipher.doFinal(data.toByteArray())
        return Base64.toBase64String(cipherData)
    }
}