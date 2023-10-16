package com.sqy.plugins.auto_switch_ime

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

/**
 * 注释判断器
 */
object CommentDecider {

    /**
     * 判断是否是注释
     */
    fun isComment(psiElement: PsiElement) : Boolean {
        if (psiElement is PsiComment
                || (psiElement is PsiWhiteSpace && psiElement.prevSibling is PsiComment)) {
            return true
        }
        if (psiElement.parent is PsiDocCommentBase) {
            if (psiElement is PsiWhiteSpace && psiElement.prevSibling.parent is PsiDocCommentBase) {
                return true
            }
        }
        return false
    }
}