package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.sqy.plugins.support.IMESwitchSupport

/**
 * 处理回车键可能导致的输入法自动切换
 */
object EnterCausedIMESwitchHandler : IMESwitchHandler {

    override fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (caretPositionChange == 1) {
            IMESwitchSupport.switchToEn()
        } else if(caretPositionChange > 1){
            handleEnterWhenMultipleCaretPositionChange(language, psiElement, isLineEnd)
        }
    }

}