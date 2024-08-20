package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    override fun getLanguage(): Language {
        return KotlinLanguage.INSTANCE
    }

}