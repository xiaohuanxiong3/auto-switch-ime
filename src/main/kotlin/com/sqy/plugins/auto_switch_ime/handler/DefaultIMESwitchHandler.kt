package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.psi.PsiElement

/**
 * 默认的输入法自动切换处理器
 */
object DefaultIMESwitchHandler : IMESwitchHandler {

    override fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        // do nothing
    }

}