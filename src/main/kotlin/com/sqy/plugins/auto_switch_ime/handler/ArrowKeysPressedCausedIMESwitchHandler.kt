package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.psi.PsiElement

/**
 * 处理按下方向键可能导致的输入法自动切换
 */
object ArrowKeysPressedCausedIMESwitchHandler : IMESwitchHandler {

    private var lastMoveTime : Long = 0
    private const val MOVE_ALLOW_INTERVAL : Long = 300

    override fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        if (System.currentTimeMillis() - lastMoveTime > MOVE_ALLOW_INTERVAL) {
            switch(language, psiElement, isLineEnd)
            lastMoveTime = System.currentTimeMillis()
        }
    }

}