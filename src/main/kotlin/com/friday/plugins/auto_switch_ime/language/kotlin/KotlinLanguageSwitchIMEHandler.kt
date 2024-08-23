package com.friday.plugins.auto_switch_ime.language.kotlin

import com.friday.plugins.auto_switch_ime.handler.AbstractSingleLanguageSwitchIMEHandler
import com.intellij.lang.Language
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private val interestCharSet : List<Char> = listOf('/', '*')

    override fun isInterestChar(c: Char): Boolean {
        return interestCharSet.contains(c)
    }

    override fun getLanguage(): Language {
        return KotlinLanguage.INSTANCE
    }

}