package com.sqy.plugins.translation

data class TranslationResult(
        val srcLanguage: String,
        val targetLanguage: String,
        val original: String,
        val translation: String?
)