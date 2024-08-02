package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

object PsiTreeChangeHandlerDelegate {

    fun getPsiTreeChildChangedHandler(language: Language) : PsiTreeChangedHandler? {
        return when(language) {
            JavaLanguage.INSTANCE -> JavaPsiTreeChangedHandler
            KotlinLanguage.INSTANCE -> KotlinPsiTreeChangedHandler
            else -> null
        }
    }

}