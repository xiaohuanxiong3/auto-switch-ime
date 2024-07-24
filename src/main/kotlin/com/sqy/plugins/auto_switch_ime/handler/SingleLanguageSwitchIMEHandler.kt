package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.auto_switch_ime.Constants
import com.sqy.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.sqy.plugins.auto_switch_ime.cause.CaretPositionChangeCause
import com.sqy.plugins.auto_switch_ime.cause.CaretPositionChangeCause.*
import com.sqy.plugins.auto_switch_ime.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage

/**
 * 单语言输入法自动切换处理器
 */
interface SingleLanguageSwitchIMEHandler {
    
    fun handle(cause: CaretPositionChangeCause, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean) {
        when (cause.description) {
            CHAR_TYPED.description -> handleCharTyped(caretPositionChange, psiElement, isLineEnd)
            MOUSE_CLICKED.description -> handleMouseClicked(caretPositionChange, psiElement, isLineEnd)
            ARROW_KEYS_PRESSED.description -> handleArrowKeysPressed(caretPositionChange, psiElement, isLineEnd)
            ENTER.description -> handleEnter(caretPositionChange, psiElement, isLineEnd)
            else -> {
                throw Error(Constants.UNREACHABLE_CODE + "in SingleLanguageSwitchIMEHandler.handle method")
            }
        }
    }
    
    fun handleMouseClicked(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleEnter(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleCharTyped(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean)
    
    fun handleArrowKeysPressed(caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean)

    fun getLanguage(): Language

    /**
     * 默认切换方法
     */
    fun switch(psiElement: PsiElement, isLineEnd: Boolean) {
        if (AreaDeciderDelegate.isCommentArea(getLanguage(), psiElement, isLineEnd)) {
            IMESwitchSupport.switchToZh()
        } else if (AreaDeciderDelegate.isCodeArea(getLanguage(), psiElement, isLineEnd)){
            IMESwitchSupport.switchToEn()
        }
    }

    /**
     * 处理回车引起的多次光标移动
     */
    fun handleEnterWhenMultipleCaretPositionChange(psiElement: PsiElement, isLineEnd : Boolean) {
        when(getLanguage()) {
            JavaLanguage.INSTANCE -> {
                psiElement.let {
                    it as? LeafPsiElement
                }?.let {
                    // 文档注释区行首
                    if (it.treePrev.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                        && (it.treeNext.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                                || it.treeNext.toString() == "PsiDocToken:DOC_COMMENT_END")) {
                        IMESwitchSupport.switchToZh()
                    }
                }
            }
            KotlinLanguage.INSTANCE -> {

            }
            else -> {
                // doNothing
            }
        }
    }
    
}