package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.sqy.plugins.auto_switch_ime.Map
import com.sqy.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.sqy.plugins.auto_switch_ime.support.IMESwitchSupport

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private var lastMoveTime : Long = 0
    private const val MOVE_ALLOW_INTERVAL : Long = 300

    override fun handleMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleEnter(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        if (caretPositionChange == 1) {
            IMESwitchSupport.switchToEn()
            Map.psiElementLocationMap[editor]!!.reset()
        } else if(caretPositionChange > 1){
            handleEnterWhenMultipleCaretPositionChange(editor, psiElement, isLineEnd)
        }
    }

    override fun handleCharTyped(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        if (isLineEnd && psiElement.prevSibling is PsiErrorElement) {
            IMESwitchSupport.switchToZh()
            Map.psiElementLocationMap[editor]!!.setLocationId(psiElement)
            Map.psiElementLocationMap[editor]!!.isSecondLanguageEnabled = true
            Map.psiElementLocationMap[editor]!!.isCommentArea = true
        }
    }

    override fun handleArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        if (System.currentTimeMillis() - lastMoveTime > MOVE_ALLOW_INTERVAL) {
            switch(editor, psiElement, isLineEnd)
            lastMoveTime = System.currentTimeMillis()
        }
    }

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }

    override fun handleEnterWhenMultipleCaretPositionChange(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        val psiElementLocation = AreaDeciderDelegate.getPsiElementLocation(getLanguage(), psiElement, isLineEnd)
        if (psiElementLocation.locationId.startsWith("PsiDocComment")) {
            IMESwitchSupport.switchToZh()
            Map.psiElementLocationMap[editor]!!.copyFrom(psiElementLocation)
        }
    }
}