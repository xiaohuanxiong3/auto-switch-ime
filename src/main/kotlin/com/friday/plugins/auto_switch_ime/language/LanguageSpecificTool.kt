package com.friday.plugins.auto_switch_ime.language

import com.friday.plugins.auto_switch_ime.areaDecide.AreaDecider
import com.friday.plugins.auto_switch_ime.handler.SingleLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.language.java.JavaAreaDecider
import com.friday.plugins.auto_switch_ime.language.java.JavaLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.language.kotlin.KotlinAreaDecider
import com.friday.plugins.auto_switch_ime.language.kotlin.KotlinLanguageSwitchIMEHandler
import com.friday.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.friday.plugins.auto_switch_ime.ui.AutoSwitchIMEStatusBarIconWidget
import com.intellij.lang.Language

/**
 * @Author Handsome Young
 * @Date 2024/12/20 20:46
 */
object LanguageSpecificTool {

    /**
     * 根据语言获取区域判断对象
     */
    fun getAreaDecider(language: Language) : AreaDecider? {
        return when(language.id) {
            "JAVA" -> JavaAreaDecider
            "kotlin" -> KotlinAreaDecider
            else -> null
        }
    }

    /**
     * 根据语言获取单语言输入法自动切换处理器
     */
    fun getSingleLanguageSwitchIMEHandler(language: Language) : SingleLanguageSwitchIMEHandler? {
        return when(language.id) {
            "JAVA" -> JavaLanguageSwitchIMEHandler
            "kotlin" -> KotlinLanguageSwitchIMEHandler
            else -> null
        }
    }

    /**
     * 判断是否开启某语言输入法自动切换功能
     */
    fun isLanguageAutoSwitchEnabled(language: Language?) : Boolean {
        return language?.let { when(it.id) {
            "JAVA" -> SwitchIMESettings.instance.isJavaEnabled
            "kotlin" -> SwitchIMESettings.instance.isKotlinEnabled
            else -> false
        }  } ?: false
    }

    fun getLanguageAutoSwitchStatus(language: Language?) : AutoSwitchIMEStatusBarIconWidget.IconStatus {
        return language?.let { when(it.id) {
            "JAVA" -> if (SwitchIMESettings.instance.isJavaEnabled) AutoSwitchIMEStatusBarIconWidget.IconStatus.ENABLE else AutoSwitchIMEStatusBarIconWidget.IconStatus.DISABLE
            "kotlin" -> if (SwitchIMESettings.instance.isKotlinEnabled) AutoSwitchIMEStatusBarIconWidget.IconStatus.ENABLE else AutoSwitchIMEStatusBarIconWidget.IconStatus.DISABLE
            else -> AutoSwitchIMEStatusBarIconWidget.IconStatus.DEACTIVATE
        } } ?: AutoSwitchIMEStatusBarIconWidget.IconStatus.DEACTIVATE
    }

    fun toggleIMESwitchSetting(language: Language?) {
        language?.let {
            when (it.id) {
                "JAVA" -> SwitchIMESettings.instance.isJavaEnabled = !SwitchIMESettings.instance.isJavaEnabled
                "kotlin" -> SwitchIMESettings.instance.isKotlinEnabled = !SwitchIMESettings.instance.isKotlinEnabled
                else -> {}
            }
        }
    }

}