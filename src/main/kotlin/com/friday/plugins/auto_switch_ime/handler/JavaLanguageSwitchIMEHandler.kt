package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private val shouldHandleCharSet : List<Char> = listOf('/', '*','"')

    override fun shouldHandleWhenCharTyped(c: Char): Boolean {
        return shouldHandleCharSet.contains(c)
    }

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }

}