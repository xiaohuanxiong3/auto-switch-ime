package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

object SingleLanguageSwitchIMEDelegate {

    fun handle(language: Language, trigger: IMESwitchTrigger, caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.handle(trigger, caretPositionChange, editor, psiElement, isLineEnd)
    }

    fun shouldHandleWhenCharTyped(language: Language, char: Char) : Boolean{
        return PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.shouldHandleWhenCharTyped(char)?:false
    }

}
