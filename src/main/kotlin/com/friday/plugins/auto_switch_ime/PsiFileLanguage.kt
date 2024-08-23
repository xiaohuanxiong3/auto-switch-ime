package com.friday.plugins.auto_switch_ime

import com.friday.plugins.auto_switch_ime.areaDecide.AreaDecider
import com.friday.plugins.auto_switch_ime.language.java.JavaAreaDecider
import com.friday.plugins.auto_switch_ime.language.kotlin.KotlinAreaDecider
import com.friday.plugins.auto_switch_ime.language.java.JavaLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.language.kotlin.KotlinLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.handler.SingleLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

enum class PsiFileLanguage(
    val language: Language,
) {

    JAVA(JavaLanguage.INSTANCE),
    KOTLIN(KotlinLanguage.INSTANCE);

    companion object {

        /**
         * 根据语言获取区域判断对象
         */
        fun getAreaDecider(language: Language) : AreaDecider? {
            return when(language) {
                JavaLanguage.INSTANCE -> JavaAreaDecider
                KotlinLanguage.INSTANCE -> KotlinAreaDecider
                else -> null
            }
        }

        /**
         * 根据语言获取单语言输入法自动切换处理器
         */
        fun getSingleLanguageSwitchIMEHandler(language: Language) : SingleLanguageSwitchIMEHandler? {
            return when(language) {
                JavaLanguage.INSTANCE -> JavaLanguageSwitchIMEHandler
                KotlinLanguage.INSTANCE -> KotlinLanguageSwitchIMEHandler
                else -> null
            }
        }

        /**
         * 判断是否开启某语言输入法自动切换功能
         */
        fun isLanguageAutoSwitchEnabled(language: Language?) : Boolean {
            return when (language) {
                JavaLanguage.INSTANCE -> SwitchIMESettings.instance.isJavaEnabled
                KotlinLanguage.INSTANCE -> SwitchIMESettings.instance.isKotlinEnabled
                else -> false
            }
        }
    }
}