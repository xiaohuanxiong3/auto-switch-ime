package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * @Author Handsome Young
 * @Date 2024/8/23 21:19
 */
interface SwitchIMEHandler {

    fun handle(trigger: IMESwitchTrigger, handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean)

    fun doHandle(handleStrategy: HandleStrategy, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        when (handleStrategy) {
            HandleStrategy.UPDATE_LOCATION -> updatePsiElementLocation(editor, psiElement, isLineEnd)
            HandleStrategy.UPDATE_LOCATION_AND_SWITCH -> updatePsiElementLocationAndSwitch(editor, psiElement, isLineEnd)
            else -> {
                throw Error(Constants.UNREACHABLE_CODE + "in SwitchIMEHandler.doHandle method")
            }
        }
    }

    fun updatePsiElementLocation(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean)

    fun updatePsiElementLocationAndSwitch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean)

}