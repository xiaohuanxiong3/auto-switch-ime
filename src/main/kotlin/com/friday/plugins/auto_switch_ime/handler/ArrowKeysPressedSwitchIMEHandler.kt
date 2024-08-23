package com.friday.plugins.auto_switch_ime.handler

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * @Author Handsome Young
 * @Date 2024/8/23 21:22
 */
interface ArrowKeysPressedSwitchIMEHandler : SwitchIMEHandler {

    fun handleWhenArrowKeysPressed(handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        doHandle(handleStrategy, editor, psiElement, isLineEnd)
    }

}