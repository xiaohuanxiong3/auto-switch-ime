package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.sqy.plugins.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage

/**
 * 处理字符输入可能导致的输入法自动切换
 */
object CharTypedCausedIMESwitchHandler : IMESwitchHandler {

    override fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (language == JavaLanguage.INSTANCE || language == KotlinLanguage.INSTANCE) {
            if (isLineEnd && psiElement.prevSibling is PsiErrorElement) {
                IMESwitchSupport.switchToZh()
            }
        }
    }

}