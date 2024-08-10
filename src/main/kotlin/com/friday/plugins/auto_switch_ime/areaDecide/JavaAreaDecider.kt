package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.impl.source.tree.ElementType
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
import com.intellij.psi.javadoc.PsiDocComment

object JavaAreaDecider : AreaDecider {

    override fun isCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return isSingleLineCommentArea(psiElement, isLineEnd)
                || isDocCommentArea(psiElement, isLineEnd)
    }

    override fun isCodeArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return true
    }

    /**
     * 是否是单行注释区域
     */
    private fun isSingleLineCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        // 单行注释区
        if (psiElement.toString() == "PsiComment(END_OF_LINE_COMMENT)") {
            return true
        }
        // 单行注释区末尾（这里会将文档注释（多行注释）的最后的空格也判断为单行注释区，暂时不做处理）
        if (isLineEnd && psiElement.prevSibling.toString() == "PsiComment(END_OF_LINE_COMMENT)") {
            return true
        }
        return false
    }

    /**
     * 是否是文档注释区域
     */
    private fun isDocCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        // 文档注释区
        if ((psiElement.parent != null && psiElement.parent is PsiDocComment)
            || (psiElement.parent.parent != null && psiElement.parent.parent is PsiDocComment)
            || (psiElement.parent.parent.parent != null && psiElement.parent.parent.parent is PsiDocComment)) {
            return true
        }
        return false
    }

    override fun getPsiElementLocation(psiElement: PsiElement, isLineEnd: Boolean): PsiElementLocation {
        val psiElementLocation = PsiElementLocation()
        // 单行注释区
        if (psiElement.toString() == "PsiComment(END_OF_LINE_COMMENT)") {
            psiElementLocation.setLocationId(psiElement.prevSibling,psiElement.nextSibling)
            psiElementLocation.isCommentArea = true
            psiElementLocation.isSecondLanguageEnabled = true
        }
        // 单行注释区末尾（这里会将文档注释（多行注释）的最后的空格也判断为单行注释区，暂时不做处理）
        else if (isLineEnd && psiElement.prevSibling != null && psiElement.prevSibling.toString() == "PsiComment(END_OF_LINE_COMMENT)") {
            psiElementLocation.setLocationId(psiElement.prevSibling.prevSibling,psiElement)
            psiElementLocation.isCommentArea = true
            psiElementLocation.isSecondLanguageEnabled = true
        }
        // 文档注释区
        else if (psiElement.parent != null) {
            if (psiElement.parent is PsiDocComment) {
                psiElementLocation.setLocationId(psiElement.parent)
                psiElementLocation.isCommentArea = true
                psiElementLocation.isSecondLanguageEnabled = true
            } else if (psiElement.parent.parent != null) {
                if (psiElement.parent.parent is PsiDocComment) {
                    psiElementLocation.setLocationId(psiElement.parent.parent)
                    psiElementLocation.isCommentArea = true
                    psiElementLocation.isSecondLanguageEnabled = true
                } else if (psiElement.parent.parent.parent != null && psiElement.parent.parent.parent is PsiDocComment) {
                    psiElementLocation.setLocationId(psiElement.parent.parent.parent)
                    psiElementLocation.isCommentArea = true
                    psiElementLocation.isSecondLanguageEnabled = true
                }
            }
        }
        // 非注释区
        else {
            if (psiElement.parent != null && psiElement.parent is PsiLiteralExpression) {
                psiElement.parent.let {
                    it as? PsiLiteralExpressionImpl
                }?.let {
                    if (ElementType.STRING_LITERALS.contains(it.literalElementType)) {
                        psiElementLocation.setLocationId(psiElement.parent)
                        psiElementLocation.isSecondLanguageEnabled = true
                        return psiElementLocation
                    }
                }
            }
            psiElementLocation.setLocationId(psiElement)
        }
        return psiElementLocation
    }

    override fun language(): Language {
        return JavaLanguage.INSTANCE
    }
}