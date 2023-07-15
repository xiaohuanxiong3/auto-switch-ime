package com.sqy.plugins.translate

data class TranslationResult(
        val srcLanguage: String,
        val targetLanguage: String,
        val original: String,
        val translation: String?
)