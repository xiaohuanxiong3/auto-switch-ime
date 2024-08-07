package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

/**
 * Java输入法自动切换处理器
 */
object JavaLanguageSwitchIMEHandler : AbstractSingleLanguageSwitchIMEHandler() {

    private var lastMoveTime : Long = 0
    private const val MOVE_ALLOW_INTERVAL : Long = 300

    override fun handleMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        if (System.currentTimeMillis() - lastMoveTime > MOVE_ALLOW_INTERVAL) {
            switch(editor, psiElement, isLineEnd)
            lastMoveTime = System.currentTimeMillis()
        }
    }

    override fun handlePsiFileChanged(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun getLanguage(): Language {
        return JavaLanguage.INSTANCE
    }

}