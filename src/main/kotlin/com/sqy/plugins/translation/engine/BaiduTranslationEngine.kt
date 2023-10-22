package com.sqy.plugins.translation.engine

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.util.io.DigestUtil
import com.intellij.util.io.HttpRequests
import com.sqy.plugins.icons.TranslationIcons
import com.sqy.plugins.translation.TranslationResult
import com.sqy.plugins.translation.TranslationResultException
import com.sqy.plugins.translation.setting.TranslationSettings
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.util.*
import javax.swing.Icon

object BaiduTranslationEngine : TranslationEngine {

    override val id: String
        get() = "baidu"
    override val name: String
        get() = "百度翻译"
    override val icon: Icon
        get() = TranslationIcons.Engines.Baidu
    override val primaryLanguage: String
        get() = settings.primaryLanguage

    private val settings : TranslationSettings = TranslationSettings.instance

    private val translateUrl : String
        get() {
            return "http://api.fanyi.baidu.com/api/trans/vip/translate"
        }

    private val gson : Gson = Gson()

    override fun translate(text: String, sourceLanguage: String, targetLanguage: String): TranslationResult {
        // TODO("Not yet implemented")
        val appId = settings.baiduTranslationEngineSetting.appId
        val secret = settings.baiduTranslationEngineSetting.appSecret
        val salt = System.currentTimeMillis().toString()
        val sign = (appId + text + salt + secret).md5().lowercase(Locale.getDefault())

        val data = mapOf (
            "appid" to appId,
            "from" to sourceLanguage,
            "to" to targetLanguage,
            "salt" to salt,
            "sign" to sign,
            "q" to text
        ).entries.joinToString("&") {(key, value) ->
            "$key=${value.urlEncode()}"
        }

         val response = HttpRequests.post(translateUrl,"application/x-www-form-urlencoded")
                 .accept("application/json")
                 .throwStatusCodeException(false)
                 .connect {
                     it.write(data)
                     with(it) {
                         val responseCode = (connection as HttpURLConnection).responseCode
                         if (responseCode >= 400) {
                             throw HttpRequests.HttpStatusException("Request failed with status code $responseCode", responseCode, url)
                         }
                     }
                     it.readString()
                 }
        val result = gson.fromJson(response,BaiduTranslationResult::class.java).apply {
            if (!isSuccessful) {
                throw TranslationResultException(code)
            }
        }.from()
        return result
    }

    override fun translate(text: String) : TranslationResult {
        val sourceLanguage = "auto"
        val targetLanguage = primaryLanguage
        return translate(text,sourceLanguage,targetLanguage)
    }

    private fun String.md5(): String {
        // MD5
        return with(DigestUtil.md5()) {
            update(toByteArray(Charsets.UTF_8))
            digest().toHexString()
        }
    }

    private val HEX_DIGITS = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    )

    private fun ByteArray.toHexString(): String {
        val result = CharArray(size * 2)
        forEachIndexed { index, byte ->
            result[index * 2] = HEX_DIGITS[byte.toInt() ushr 4 and 0xf]
            result[index * 2 + 1] = HEX_DIGITS[byte.toInt() and 0xf]
        }

        return String(result)
    }

    private fun String.urlEncode(): String =
            if (isEmpty()) this else URLEncoder.encode(this, "UTF-8")


    data class BaiduTranslationResult(
            @SerializedName("error_code")
            val code: Int = 0,
            @SerializedName("from")
            val srcLanguage: String? = null,
            @SerializedName("to")
            val targetLanguage: String? = null,
            @SerializedName("trans_result")
            val trans: List<BTrans> = emptyList()
    ) {
        val isSuccessful get() = code == 0 || code == 52000

        fun from() : TranslationResult{

            check(srcLanguage != null) { "srcLanguage 为空" }
            check(targetLanguage != null) { "targetLanguage 为空" }
            check(trans.isNotEmpty()) { "翻译结果为空" }

            val original = StringBuilder()
            val translation = StringBuilder()

            trans.forEachIndexed { index, (src, dst) ->
                if (index > 0) {
                    original.append('\n')
                    translation.append('\n')
                }
                original.append(src)
                translation.append(dst)
            }

            return TranslationResult(srcLanguage,targetLanguage,original.toString(),translation.toString())
        }
    }

    data class BTrans(
            @SerializedName("src")
            val src: String,
            @SerializedName("dst")
            val dst: String
    )
}