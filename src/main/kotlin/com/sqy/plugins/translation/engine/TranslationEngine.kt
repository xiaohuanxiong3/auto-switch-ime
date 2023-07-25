package com.sqy.plugins.translation.engine

import com.sqy.plugins.translation.TranslationResult
import javax.swing.Icon

interface TranslationEngine : Translate {

    val id : String

    val name: String

    val icon: Icon

    /**
     * 主语言，用于设置默认的翻译目标语言
     */
    val primaryLanguage : String

    fun translate(text : String) : TranslationResult


}