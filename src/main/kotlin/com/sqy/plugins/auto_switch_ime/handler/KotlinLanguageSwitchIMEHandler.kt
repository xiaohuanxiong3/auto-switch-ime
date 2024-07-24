package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinLanguageSwitchIMEHandler : SingleLanguageSwitchIMEHandler {

    override fun handleMouseClicked(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleEnter(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleCharTyped(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleArrowKeysPressed(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): Language {
        return KotlinLanguage.INSTANCE
    }
}