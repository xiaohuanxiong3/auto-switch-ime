package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * 单语言输入法自动切换处理器
 */
interface SingleLanguageSwitchIMEHandler {


    fun handle(trigger: IMESwitchTrigger, caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)

    fun handleArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)

    fun handlePsiFileChanged(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)

    fun switch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean)

    fun getLanguage(): Language
    
}