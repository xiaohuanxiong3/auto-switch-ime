package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage

object AreaDeciderDelegate {

    fun isCommentArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean{
        return PsiFileLanguage.getAreaDecider(language)?.isCommentArea(psiElement, isLineEnd)?:false
    }

    fun isCodeArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean {
        return PsiFileLanguage.getAreaDecider(language)?.isCodeArea(psiElement, isLineEnd)?:false
    }

    /**
     * 处理回车事件引起的多次光标移动
     */
    fun handleEnterWhenMultipleCaretPositionChange(language : Language,psiElement: LeafPsiElement, isLineEnd : Boolean) {
        when(language) {
            JavaLanguage.INSTANCE -> {
                // 文档注释区行首
                if (psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                        && psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS") {
                    IMESwitchSupport.switchToZh()
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