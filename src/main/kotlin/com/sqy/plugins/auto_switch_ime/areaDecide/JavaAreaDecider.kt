package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.javadoc.PsiDocComment

object JavaAreaDecider : AreaDecider {

    override fun isCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        // 单行注释区
        if (psiElement is PsiComment) {
            return true
        }
        // 单行注释区末尾（这里会将文档注释（多行注释）的最后的空格也判断为单行注释区，暂时不做处理）
        if (isLineEnd && psiElement.prevSibling is PsiComment) {
            return true
        }
        // 文档注释区
        if ((psiElement.parent != null && psiElement.parent is PsiDocComment)
            || (psiElement.parent.parent != null && psiElement.parent.parent is PsiDocComment)
            || (psiElement.parent.parent.parent != null && psiElement.parent.parent.parent is PsiDocComment)) {
            return true
        }
        return false
    }

    @Deprecated("")
    fun isCommentArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
        // 单行注释区
        if (psiElement is PsiComment) {
            return true
        }
        // 单行注释区末尾（这里会将文档注释（多行注释）的最后的空格也判断为单行注释区，暂时不做处理）
        if (isLineEnd && psiElement.prevSibling is PsiComment) {
            return true
        }
        // 文档注释区
        if ((psiElement.parent != null && psiElement.parent is PsiDocComment)
            || (psiElement.parent.parent != null && psiElement.parent.parent is PsiDocComment)
            || (psiElement.parent.parent.parent != null && psiElement.parent.parent.parent is PsiDocComment)) {
            return true
        }
//        if (psiElement.toString() == "PsiDocToken:DOC_COMMENT_DATA") {
//            return true
//        }
//        if (psiElement.treePrev == null) {
//            return false
//        }
//
//        // 文档注释区行尾
//        if (isLineEnd && psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_DATA") {
//            return true
//        }
//        // 文档区，行尾，前面是类似 @param xxx
//        if (isLineEnd && (psiElement.treePrev is PsiDocTag
//                    || psiElement.treePrev.treeParent is PsiDocTag)) {
//            return true
//        }
//        if (psiElement.treeNext == null) {
//            return false
//        }
//        // 文档注释区行首
//        if (psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
//            && (psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
//                        || psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_END")) {
//            return true
//        }
        return false
    }

    override fun isCodeArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return true
    }

    @Deprecated("")
    fun isCodeArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
//        if (psiElement is PsiIdentifier
//            || psiElement.treeParent is PsiExpression
//            || psiElement.treeParent is PsiExpressionList
//            || psiElement.treeParent is PsiStatement
//            || (!isLineEnd && psiElement is PsiWhiteSpace)
//            || isLineEnd && psiElement.treePrev != null
//            && (psiElement.treePrev is PsiExpression
//                    || psiElement.treePrev is PsiStatement
//                    || psiElement.treePrev is Constants
//                    || psiElement.treePrev is PsiKeyword
//                    || psiElement.treePrev is PsiErrorElement)) {
//            return true
//        }
//        return false
        return true
    }

    override fun language(): Language {
        return JavaLanguage.INSTANCE
    }
}