package com.friday.plugins.auto_switch_ime.handler

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * @Author Handsome Young
 * @Date 2024/8/23 21:26
 */
interface CharTypedSwitchIMEHandler : SwitchIMEHandler {

    fun getHandleStrategyWhenCharTyped(c: Char, editor: Editor) : HandleStrategy

    fun isInterestChar(c: Char): Boolean

    fun handleWhenCharTyped(handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        doHandle(handleStrategy, editor, psiElement, isLineEnd)
    }

}