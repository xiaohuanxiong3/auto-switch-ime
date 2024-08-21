package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private val shouldHandleCharSet : List<Char> = listOf('/', '*')

    override fun shouldHandleWhenCharTyped(c: Char): Boolean {
        return shouldHandleCharSet.contains(c)
    }

    override fun getLanguage(): Language {
        return KotlinLanguage.INSTANCE
    }

}