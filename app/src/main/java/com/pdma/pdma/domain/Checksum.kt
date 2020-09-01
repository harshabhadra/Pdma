package com.pdma.pdma.domain

import com.easypay.epmoney.epmoneylib.utils.Utility.Companion.generateToken
import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Checksum {
    var currentTime = 0L
    var randomNumber = ""

    @Throws(
        DecoderException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        UnsupportedEncodingException::class
    )
    fun getCheckSum(agentCode: String): String {

        currentTime = Calendar.getInstance().timeInMillis
        randomNumber =
            generateToken(15)
        println(currentTime)
        val data =
            "$agentCode|$currentTime|$randomNumber"
        val secretKey = "f85acd0c3c"//  aefc05467d
        val decodedKey = Hex.decodeHex(secretKey.toCharArray())
        val keySpec =
            SecretKeySpec(decodedKey, "HmacSHA512")
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(keySpec)
        val dataBytes = data.toByteArray(charset("UTF-8"))
        val signatureBytes = mac.doFinal(dataBytes)
        val signature =
            String(Base64.encodeBase64(signatureBytes), StandardCharsets.UTF_8)
        println("CheckSum--> $signature")
        return signature
    }
}