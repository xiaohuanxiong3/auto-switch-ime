package com.sqy.plugins.translation.engine

import com.sqy.plugins.icons.TranslationIcons
import com.sqy.plugins.translation.TranslationResult
import javax.swing.Icon

object DoNothingTranslationEngine : TranslationEngine{

    override val id: String
        get() = "doNothing"
    override val name: String
        get() = "默认不翻译"
    override val icon: Icon
        get() = TranslationIcons.Translation
    override val primaryLanguage: String
        get() = "cn"

    override fun translate(text: String): TranslationResult {
        // TODO("Not yet implemented")
        return translate(text,"auto", primaryLanguage)
    }

    override fun translate(text: String, sourceLanguage: String, targetLanguage: String): TranslationResult {
        // TODO("Not yet implemented")
        return TranslationResult(primaryLanguage, primaryLanguage,text,text)
    }
}