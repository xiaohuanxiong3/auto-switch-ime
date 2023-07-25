package com.sqy.plugins.translation.setting.engineSettings

import com.intellij.openapi.diagnostic.Logger
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class BaiduTranslationEngineSetting {

    @Transient
    private val logger : Logger = Logger.getInstance(BaiduTranslationEngineSetting::class.java)

    var appId : String by Delegates.observable("") { _: KProperty<*>, oldValue: String, newValue: String ->
        if (oldValue != newValue) {
            logger.info("百度翻译设置:appId changed")
        }
    }

    var appSecret : String by Delegates.observable("") { _: KProperty<*>, oldValue: String, newValue: String ->
        if (oldValue != newValue) {
            logger.info("百度翻译设置:appSecret changed")
        }
    }
}