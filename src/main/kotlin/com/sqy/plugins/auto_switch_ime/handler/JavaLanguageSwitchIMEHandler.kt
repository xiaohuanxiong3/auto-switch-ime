package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.sqy.plugins.auto_switch_ime.support.IMESwitchSupport

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : SingleLanguageSwitchIMEHandler {

    private var lastMoveTime : Long = 0
    private const val MOVE_ALLOW_INTERVAL : Long = 300

    override fun handleMouseClicked(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(psiElement, isLineEnd)
    }

    override fun handleEnter(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (caretPositionChange == 1) {
            IMESwitchSupport.switchToEn()
        } else if(caretPositionChange > 1){
            handleEnterWhenMultipleCaretPositionChange(psiElement, isLineEnd)
        }
    }

    override fun handleCharTyped(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (isLineEnd && psiElement.prevSibling is PsiErrorElement) {
            IMESwitchSupport.switchToZh()
        }
    }

    override fun handleArrowKeysPressed(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (System.currentTimeMillis() - lastMoveTime > MOVE_ALLOW_INTERVAL) {
            switch(psiElement, isLineEnd)
            lastMoveTime = System.currentTimeMillis()
        }
    }

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }
}