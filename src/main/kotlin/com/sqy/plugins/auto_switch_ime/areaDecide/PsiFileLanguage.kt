package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

enum class PsiFileLanguage(
    val language: Language,
) {

    JAVA(JavaLanguage.INSTANCE),
    KOTLIN(KotlinLanguage.INSTANCE);

    companion object {
        fun getAreaDecider(language: Language) : AreaDecider? {
            return when(language) {
                JavaLanguage.INSTANCE -> JavaAreaDecider
                KotlinLanguage.INSTANCE -> KotlinAreaDecider
                else -> null
            }
        }
    }
}