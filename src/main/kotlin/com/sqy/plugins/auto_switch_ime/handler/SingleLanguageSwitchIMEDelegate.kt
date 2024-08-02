package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.sqy.plugins.auto_switch_ime.PsiFileLanguage
import com.sqy.plugins.auto_switch_ime.cause.CaretPositionChangeCause

object SingleLanguageSwitchIMEDelegate {

    fun handle(language: Language, cause: CaretPositionChangeCause, caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.handle(cause, caretPositionChange, editor, psiElement, isLineEnd)
    }

}
