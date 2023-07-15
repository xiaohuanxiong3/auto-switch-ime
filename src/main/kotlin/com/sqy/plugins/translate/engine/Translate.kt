package com.sqy.plugins.translate.engine

import com.sqy.plugins.translate.TranslationResult

interface Translate {

    fun translate(text:String, sourceLanguage:String, targetLanguage:String) : TranslationResult

}