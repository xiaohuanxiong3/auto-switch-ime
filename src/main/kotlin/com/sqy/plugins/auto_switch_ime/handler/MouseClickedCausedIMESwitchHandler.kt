package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.psi.PsiElement

/**
 * 处理鼠标点击可能导致的输入法自动切换
 */
object MouseClickedCausedIMESwitchHandler : IMESwitchHandler {

    override fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(language, psiElement, isLineEnd)
    }

}