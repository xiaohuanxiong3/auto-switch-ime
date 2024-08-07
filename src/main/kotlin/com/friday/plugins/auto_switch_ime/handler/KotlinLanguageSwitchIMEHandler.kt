package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    override fun handleMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {

    }

    override fun handleArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {

    }

    override fun handlePsiFileChanged(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {

    }

    override fun switch(editor: Editor, curPsiElement: PsiElement, isLineEnd: Boolean) {

    }

    override fun getLanguage(): Language {
        return KotlinLanguage.INSTANCE
    }

}