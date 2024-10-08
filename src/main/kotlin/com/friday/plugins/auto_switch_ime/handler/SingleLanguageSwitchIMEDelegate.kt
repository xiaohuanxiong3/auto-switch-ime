package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.language.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

object SingleLanguageSwitchIMEDelegate {

    fun getHandleStrategyWhenCharTyped(language: Language, c: Char, editor: Editor): HandleStrategy {
        return PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.getHandleStrategyWhenCharTyped(c, editor)?:HandleStrategy.DO_NOT_HANDLE
    }

    fun getHandleStrategyWhenAnActionHappened(language: Language, action: AnAction, documentChange: Int, caretChange: Int) : HandleStrategy {
        return PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.getHandleStrategyWhenAnActionHappened(action, documentChange, caretChange)?:HandleStrategy.DO_NOT_HANDLE
    }

    fun handle(language: Language, trigger: IMESwitchTrigger, handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        PsiFileLanguage.getSingleLanguageSwitchIMEHandler(language)?.handle(trigger, handleStrategy, editor, psiElement, isLineEnd)
    }

}
