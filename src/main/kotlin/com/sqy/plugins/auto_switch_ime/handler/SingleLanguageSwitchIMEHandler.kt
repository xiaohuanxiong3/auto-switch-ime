package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.sqy.plugins.auto_switch_ime.cause.CaretPositionChangeCause

/**
 * 单语言输入法自动切换处理器
 */
interface SingleLanguageSwitchIMEHandler {


    fun handle(cause: CaretPositionChangeCause, caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleEnter(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleCharTyped(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)

    fun switch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean)

    fun getLanguage(): Language
    
}