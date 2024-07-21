package com.sqy.plugins.auto_switch_ime.handler

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.sqy.plugins.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage

/**
 * 输入法切换处理器
 */
interface IMESwitchHandler {

    fun handle(language: Language, caretPositionChange: Int, psiElement: PsiElement, isLineEnd: Boolean)

    /**
     * 默认切换方法
     */
    fun switch(language: Language, psiElement: PsiElement, isLineEnd: Boolean) {
        if (AreaDeciderDelegate.isCommentArea(language, psiElement, isLineEnd)) {
            IMESwitchSupport.switchToZh()
        } else if (AreaDeciderDelegate.isCodeArea(language, psiElement, isLineEnd)){
            IMESwitchSupport.switchToEn()
        }
    }

    /**
     * 处理回车引起的多次光标移动
     */
    fun handleEnterWhenMultipleCaretPositionChange(language : Language,psiElement: PsiElement, isLineEnd : Boolean) {
        when(language) {
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