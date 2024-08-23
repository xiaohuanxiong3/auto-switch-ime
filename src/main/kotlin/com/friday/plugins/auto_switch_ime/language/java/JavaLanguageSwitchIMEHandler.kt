package com.friday.plugins.auto_switch_ime.language.java

import com.friday.plugins.auto_switch_ime.handler.AbstractSingleLanguageSwitchIMEHandler
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private val interestCharSet : List<Char> = listOf('/', '*','"')

    override fun isInterestChar(c: Char): Boolean {
        return interestCharSet.contains(c)
    }

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }

}