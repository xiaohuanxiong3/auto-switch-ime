package com.sqy.plugins.auto_switch_ime

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.sqy.plugins.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage

/**
 * 进行场景判断后决定输入法切换方案
 */
object IMESwitchDelegate {

    fun switch(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean){
        if (AreaDeciderDelegate.isCommentArea(language, psiElement, isLineEnd)) {
            IMESwitchSupport.switchToZh()
        } else if (AreaDeciderDelegate.isCodeArea(language, psiElement, isLineEnd)){
            IMESwitchSupport.switchToEn()
        }
    }

    /**
     * 处理回车事件引起的多次光标移动
     */
    fun handleEnterWhenMultipleCaretPositionChange(language : Language,psiElement: LeafPsiElement, isLineEnd : Boolean) {
        when(language) {
            JavaLanguage.INSTANCE -> {
                // 文档注释区行首
                if (psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                    && (psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                            || psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_END")) {
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