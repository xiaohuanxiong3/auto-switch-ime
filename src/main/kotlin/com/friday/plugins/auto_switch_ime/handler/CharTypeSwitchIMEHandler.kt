package com.friday.plugins.auto_switch_ime.handler

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * 为了适配中文输入法，不使用 CharTypedSwitchIMEHandler
 * 中文输入法在未确认时，不会触发 EditorTyping 事件
 * @Author Handsome Young
 * @Date 2024/8/23 21:26
 */
interface CharTypeSwitchIMEHandler : SwitchIMEHandler {

    fun getHandleStrategyWhenCharTyped(c: Char, editor: Editor) : HandleStrategy

    fun isInterestChar(c: Char): Boolean

    fun handleWhenCharType(handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        doHandle(handleStrategy, editor, psiElement, isLineEnd)
    }

}