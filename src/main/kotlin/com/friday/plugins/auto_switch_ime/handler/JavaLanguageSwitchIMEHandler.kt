package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }

}