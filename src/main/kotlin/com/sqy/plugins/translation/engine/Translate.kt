package com.sqy.plugins.translation.engine

import com.sqy.plugins.translation.TranslationResult

interface Translate {

    fun translate(text:String, sourceLanguage:String, targetLanguage:String) : TranslationResult

}